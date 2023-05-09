package codegen;

import org.jooq.Constants;
import org.jooq.Record;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UniqueKeyDefinition;
import org.jooq.tools.JooqLogger;
import org.springframework.stereotype.Service;
import service.GenericServiceImpl;

import java.io.File;
import java.util.List;

/**
 * 自定义 Java 代码生成器
 * 生成 DAO
 * 生成 Entity
 * 生成 Service
 *
 * @author Diamond
 */
public class CustomFullJavaGenerator extends CustomDaoJavaGenerator {
    static final JooqLogger log = JooqLogger.getLogger(CustomFullJavaGenerator.class);

    static final String SERVICE_PACKAGE_NAME = "service";


    @Override
    protected void generatePojo(TableDefinition table) {
        super.generatePojo(table);

        // 生成Service
        generateService(table);
    }

    protected void generateService(TableDefinition table) {
        File file = getServiceFile(table);
        if (file.exists()) {
            log.info("Generating Service exists", file.getName());
            return;
        } else {
            log.info("Generating Service", file.getName());
        }
        JavaWriter out = newJavaWriter(file);
        generateService(table, out);
        closeJavaWriter(out);
    }

    protected void generateService(TableDefinition table, JavaWriter out) {
        UniqueKeyDefinition key = table.getPrimaryKey();
        if (key == null) {
            log.info("Skipping Service generation", out.file().getName());
            return;
        }

        String daoFullClassName = getStrategy().getFullJavaClassName(table, GeneratorStrategy.Mode.DAO);
        String daoType = out.ref(daoFullClassName);
        String abstractGenericService = out.ref(GenericServiceImpl.class);
        if (this.generateSpringAnnotations()) {
            out.ref(Service.class);
        }
        List<ColumnDefinition> keys = key.getKeyColumns();
        String primaryKeyType = getPrimaryKeyType(keys, out);
        String serviceClassName = getServiceClassName(table);
        String pojoType = out.ref(getEntityFullClassName(table));

        out.println("package %s;", getServiceTargetPackage());
        out.println();
        out.printImports();
        out.println();
        if (this.generateSpringAnnotations()) {
            out.println("@Service");
        }
        out.println("public class %s extends %s<%s, %s> {", serviceClassName, abstractGenericService, pojoType, primaryKeyType);
        out.println();
        out.tab(1).println("public %s(%s dao) {", serviceClassName, daoType);
        out.tab(2).println("super(dao);");
        out.tab(1).println("}");

        out.print("}");
    }

    protected String getPrimaryKeyType(List<ColumnDefinition> keyColumns, JavaWriter out) {
        String tType;
        if (keyColumns.size() == 1) {
            tType = getJavaType(keyColumns.get(0).getType(resolver()), GeneratorStrategy.Mode.POJO);
        } else if (keyColumns.size() <= Constants.MAX_ROW_DEGREE) {
            String generics = "";
            String separator = "";

            for (ColumnDefinition column : keyColumns) {
                generics += separator + out.ref(getJavaType(column.getType(resolver())));
                separator = ", ";
            }

            tType = Record.class.getName() + keyColumns.size() + "<" + generics + ">";
        } else {
            tType = Record.class.getName();
        }

        tType = out.ref(tType);
        return tType;
    }

    protected File getServiceFile(TableDefinition table) {
        String dir = getProjectTargetDirectory();
        String pkg = getServiceTargetPackage().replaceAll("\\.", "/");
        return new File(dir + "/" + pkg, getServiceFileName(table));
    }

    protected String getServiceClassName(TableDefinition definition) {
        return getTableSimpleName(definition) + "Service";
    }

    protected String getServiceTargetPackage() {
        return getTargetPackage().substring(0, getTargetPackage().lastIndexOf(".")) + "." +
                SERVICE_PACKAGE_NAME;
    }

    protected String getServiceFileName(TableDefinition definition) {
        return getServiceClassName(definition) + ".java";
    }
}

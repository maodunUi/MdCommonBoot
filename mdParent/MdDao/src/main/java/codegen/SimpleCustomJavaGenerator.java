package codegen;

import core.ExtendDAOImpl;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.impl.DAOImpl;
import org.jooq.meta.TableDefinition;
import org.jooq.tools.JooqLogger;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 简约代码生成器
 * @author laiqx
 * @date 2021-12-08
 */
public class SimpleCustomJavaGenerator extends JavaGenerator {
    static final JooqLogger log = JooqLogger.getLogger(CustomDaoJavaGenerator.class);

    /**
     * 重写了 generateDao， 具体的生成逻辑还是调用父级的方法，只是在生成完成后，获取文件内容，
     * 然后对文件指定的内容进行替换操作
     *
     * @param table
     */
    @Override
    protected void generateDao(TableDefinition table) {
        super.generateDao(table);
        File file = getFile(table, GeneratorStrategy.Mode.DAO);
        if (file.exists()) {
            try {
                String entityName = getTableSimpleName(table);
                String pojoName = entityName + "Pojo";
                String fileContent = FileCopyUtils.copyToString(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()));
                /* 替换基层DAOImpl */
                String oldExtends = " extends " + DAOImpl.class.getSimpleName();
                String newExtends = " extends " + ExtendDAOImpl.class.getSimpleName();
                newExtends = newExtends.replace(pojoName, "T");
                fileContent = fileContent.replace("import org.jooq.impl.DAOImpl;\n", "import com.shanhs.fast.dao.core.ExtendDAOImpl;\n");
                fileContent = fileContent.replace(oldExtends, newExtends);
                /* 替换掉所有的PoJo未泛型T */
                fileContent = fileContent.replace(pojoName, "T");
                /* 替换类名，将类名改为泛型T */
                String oldClassName = "public class " + entityName + "Dao";
                String newClassName = "public class " + entityName + "Dao<T extends " + entityName + "Pojo>";
                fileContent = fileContent.replace(oldClassName, newClassName);
                /* 还原引入import中被Pojo被替换未T的类名 */
                fileContent = fileContent.replace(".T;", "." + pojoName + ";");
                /* 加入初始化CLASS方法 */
                String methodExStr = "    private static Class tClass = "+pojoName+".class;\n\n"
                        + "    public static void initTargetClass(Class targetClass){tClass = targetClass;}";
                fileContent = fileContent.replace("> {", "> {\n\n" + methodExStr);
                fileContent = fileContent.replace("T.class", "tClass");
                /* 生成新的java类 */
                FileCopyUtils.copy(fileContent.getBytes(), file);
            } catch (IOException e) {
                log.error("generateDao error: {}", file.getAbsolutePath(), e);
            }
        }
    }


    @Override
    protected void generatePojo(TableDefinition table) {
        super.generatePojo(table);
    }

    /**
     * 将表名转换为驼峰式命名首字母也大写
     * s1_user -> S1User
     * hello_world -> HelloWorld
     *
     * @param definition
     * @return
     */
    protected String getTableSimpleName(TableDefinition definition) {
        return getStrategy().getJavaClassName(definition, GeneratorStrategy.Mode.DOMAIN);
    }

}

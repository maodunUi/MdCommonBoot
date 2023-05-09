package codegen;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.tools.StringUtils;

import java.util.Arrays;

/**
 * run java application
 * main class @org.jooq.util.GenerationTool
 * arg: /jooq-code-gen.xml
 *
 * @author Diamond
 */
public class CustomGeneratorStrategy extends DefaultGeneratorStrategy {

    String getDefaultJavaClassName(Definition definition) {
        // [#2032] Intercept default catalog
        if (definition instanceof CatalogDefinition && ((CatalogDefinition) definition).isDefaultCatalog()) {
            return "DefaultCatalog";
        }

            // [#2089] Intercept default schema
        else if (definition instanceof SchemaDefinition && ((SchemaDefinition) definition).isDefaultSchema()) {
            return "DefaultSchema";
        } else {
            return null;
        }
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        String name = getDefaultJavaClassName(definition);

        if (name != null) {
            return name;
        }

        // [#4562] Some characters should be treated like underscore
        String result = StringUtils.toCamelCase(
                definition.getOutputName()
                        .replace(' ', '_')
                        .replace('-', '_')
                        .replace('.', '_')
        );

        // 数据库表 bsv_channel_price 与 bsv_channelprice 生成文件名大小写不同，windows系统不支持
        // 数据库表 bsb_channel_price 与 bsb_channelprice 生成文件名大小写不同，windows系统不支持
        if (Arrays.asList("BsbChannelPrice", "BsvChannelPrice")
                .contains(result)) {
            result = result + "U";
        }

        switch (mode) {
            case INTERFACE:
                result = "I" + result;
                break;
            case DAO:
                result +="Dao";
                break;
            case RECORD:
                result +="Record";
                break;
            case POJO:
                result += "Pojo";
                break;
            case DEFAULT:
                if (definition instanceof TableDefinition) {
                    result = "T" + result;
                }
                break;
            default:
                break;
        }

        return result;
    }

}

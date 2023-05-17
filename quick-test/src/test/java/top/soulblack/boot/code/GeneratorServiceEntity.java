package top.soulblack.boot.code;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.jupiter.api.Test;
import top.soulblack.quick.common.enity.base.BaseModel;

import java.util.HashMap;
import java.util.Map;

public class GeneratorServiceEntity {
    // 文件输出路径
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "\\src\\main\\java";

    // 过滤不生成字段
    private static final String[] BASE_FIELDS = new String[]{"id", "create_time", "creator_id", "creator",
            "update_time", "updater_id", "updater", "re_mark", "version"};

    // 文件是否生成开关
    private static final Map<FileType, Boolean> FILE_SWITCH_MAP = new HashMap<>();
    static {
        FILE_SWITCH_MAP.put(FileType.ENTITY, true);
        FILE_SWITCH_MAP.put(FileType.MAPPER, true);
        FILE_SWITCH_MAP.put(FileType.XML, true);
        FILE_SWITCH_MAP.put(FileType.SERVICE, true);
        FILE_SWITCH_MAP.put(FileType.SERVICE_IMPL, true);
        FILE_SWITCH_MAP.put(FileType.CONTROLLER, true);
        FILE_SWITCH_MAP.put(FileType.OTHER, true);
    }

    // 针对哪些表生成
    private static final String[] tableNames = new String[]{
            "quick_message"
    };

    @Test
    public void generateCode() {
        String packageName = "top.soulblack.quick";
        String moduleName = null;
        boolean serviceNameStartWithI = false;//user -> UserService, 设置成true: user -> IUserService
        generateByTables(serviceNameStartWithI, packageName, moduleName, tableNames);
    }

    private void generateByTables(boolean serviceNameStartWithI, String packageName, String moduleName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();

        String dbUrl = "jdbc:mysql://localhost:3306/quick?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=UTC";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("")
                .setDriverName("com.mysql.cj.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(true) // lombok支持
                .setSuperEntityClass(BaseModel.class) // entity继承BaseModel类
                .setSuperEntityColumns(BASE_FIELDS)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setNaming(NamingStrategy.underline_to_camel)
                .setTablePrefix("quick_")
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        config.setActiveRecord(false) // 去除序列化依赖引用
                .setAuthor(System.getenv("USERNAME"))
                .setOutputDir(OUTPUT_DIR)
//                .setIdType(IdType.AUTO)
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }

        // 文件创建开关配置
        InjectionConfig injectionConfig = new InjectionConfig(){
            @Override
            public void initMap() {}
        }.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                Boolean flag = FILE_SWITCH_MAP.get(fileType);
                return flag != null && flag;
            }
        });

        PackageConfig packageInfo = new PackageConfig()
                .setParent(packageName)
                .setModuleName(moduleName)
                .setServiceImpl("service")
                .setEntity("common.entity")
                .setMapper("mapper")
                .setXml("mapper.xml");
        ConfigBuilder configBuilder = new ConfigBuilder(packageInfo, dataSourceConfig, strategyConfig, null, config).setInjectionConfig(injectionConfig);

        new AutoGenerator().setConfig(configBuilder).execute();

        // 二次生成manager文件
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setService("/templates/manager.java").setServiceImpl("/templates/managerImpl.java");
        config.setServiceName("%sManager").setServiceImplName("%sManagerImpl");
        packageInfo.setService("manager").setServiceImpl("manager");
        configBuilder = new ConfigBuilder(packageInfo, dataSourceConfig, strategyConfig, templateConfig, config).setInjectionConfig(injectionConfig);
        new AutoGenerator().setConfig(configBuilder).execute();
    }

}

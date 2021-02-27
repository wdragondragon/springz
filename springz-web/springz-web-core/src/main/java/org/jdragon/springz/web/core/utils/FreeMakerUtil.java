package org.jdragon.springz.web.core.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.13 16:57
 * @Description:
 */
public class FreeMakerUtil {

    private final String resourcePath;

    private final String suffix;

    public FreeMakerUtil(String resourcePath, String suffix) {
        this.resourcePath = resourcePath;
        this.suffix = suffix;
    }

    public Template getTemplate(String name) {
        try {
            //通过Freemarker的Configuration读取相应的ftl
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);//这里是对应的你使用jar包的版本号：<version>2.3.23</version>

            //configuration.setDirectoryForTemplateLoading(new File("/ftl"));
            //如果是maven项目可以使用这种方式
            //第二个参数 为你对应存放.ftl文件的包名
            configuration.setClassForTemplateLoading(this.getClass(), resourcePath);

            return configuration.getTemplate(name + "." + suffix);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void print(String name, Map<String, Object> root, Writer writer) {
        try {
            Template template = this.getTemplate(name);
            template.process(root, writer);//在控制台输出内容
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void print(String name, Map<String, Object> root) {
        print(name, root, new PrintWriter(System.out));
    }

    public String printString(String name, Map<String, Object> root) {
        StringWriter writer = new StringWriter();
        print(name, root, writer);
        return writer.toString();
    }

    /**
     * 输出HTML文件
     *
     * @param name
     * @param root
     * @param outFile
     */
    public void printFile(String name, Map<String, Object> root, String outFile) {
        try (FileWriter writer = new FileWriter(new File(outFile))) {
            // 通过一个文件输出流，就可以写到相应的文件中，此处用的是绝对路径
            print(name, root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

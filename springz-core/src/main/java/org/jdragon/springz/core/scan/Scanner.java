package org.jdragon.springz.core.scan;

import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.StringUtils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 15:14
 * @Description: 组件扫描注册器
 */
public class Scanner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    /**
     * 扫描文件后缀
     */
    private final String Suffix_CLASS = ".class";

    private final String RUN_JAR = "jar";

    private final String RUN_FILE = "file";


    /**
     * 文件路径间隔
     */
    private final String PATH_SEPARATOR = "/";
    /**
     * 类包路径间隔
     */
    private final String PKG_SEPARATOR = ".";

    /**
     * 扫描根路径
     */
    private final String SCAN_BASE_PACKAGE;
    /**
     * 放置bean
     */
    private final ClassLoader classLoader;

    private String[] baseClassesName;


    private ScanAction scanAction;

    private Filter filter;

    public Scanner(String...baseClassesName) {

        this.classLoader = getClass().getClassLoader();

        this.SCAN_BASE_PACKAGE = Objects.requireNonNull(classLoader.getResource("")).getPath();

        init(baseClassesName);

    }
    public void init(String...baseClassesName){
        BaseClassesContext baseClassesContext = new BaseClassesContext();
        setAction(baseClassesContext).doScan();
        filter = new Filter(baseClassesContext.getBasePackageInfoMap());
        this.baseClassesName = baseClassesContext.getBasePackages(baseClassesName);
    }

    public Scanner setAction(ScanAction scanAction) {
        this.scanAction = scanAction;
        return this;
    }

    public void doScan() {
        for(String baseClazzName:baseClassesName) {
            String baseClazzPackage = baseClazzName;
            //filter未初始化前扫类，后扫包
            baseClazzPackage = baseClazzPackage.replaceAll("\\" + PKG_SEPARATOR, PATH_SEPARATOR);
            String runType = Objects.requireNonNull(classLoader.getResource(baseClazzPackage)).getProtocol();
            logger.info("扫描", baseClazzPackage);

            if (RUN_JAR.equals(runType)) {
                this.scanJarPackage(baseClazzPackage);
            } else if (RUN_FILE.equals(runType)) {
                this.scanFilePackage(baseClazzPackage);
            }
        }
    }

    /**
     * 注意：scanJarPackage与scanPackage的差异：
     * 一、因为打包后不能直接使用File来获取resources资源
     * 需要使用jarFile来获取jarEntry再获取类信息
     * <p>
     * 二、并且要小心对待File与jarEntry的name属性差异
     * 1、File的name属性是文件名，而jarEntry的name属性是文件路径+文件名
     * 2、JarURLConnection.getJarFile().entries自带文件递归效果，不需要递归读取文件
     * <p>
     * 三、使用scanCache缓存第一次扫描的文件夹，之后对需要扫描文件夹的操作直接取cache
     **/
    private void scanJarPackage(final String jarPkg) {
        JarFile jarBaseFilePath;
        try {
            URL url = classLoader.getResource(jarPkg);
            JarURLConnection jarUrlConnection = (JarURLConnection)
                    Objects.requireNonNull(url).openConnection();

            jarBaseFilePath = jarUrlConnection.getJarFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Enumeration<JarEntry> jarFileEntries = jarBaseFilePath.entries();
        List<JarEntry> jarClass = new LinkedList<>();

        while (jarFileEntries.hasMoreElements()) {
            JarEntry jarFileEntry = jarFileEntries.nextElement();
            String jarEntryName = jarFileEntry.getName();
            //判断是否不为文件夹，且后缀是否为.class
            if (jarEntryName.contains(jarPkg) && !jarFileEntry.isDirectory() && jarEntryName.endsWith(Suffix_CLASS)) {
                jarClass.add(jarFileEntry);
            }
        }

        for (JarEntry clazzFile : jarClass) {
            String clazzFileName = clazzFile.getName();
            // 要先将最后的.class后缀删除，再将文件分隔符'/'替换成包类分隔符'.'
            clazzFileName = clazzFileName
                    .substring(0, clazzFileName.lastIndexOf(PKG_SEPARATOR))
                    .replaceAll(PATH_SEPARATOR, PKG_SEPARATOR);

            //获取到的className是全类名，需要截取最后的文件名来作为key
            //并将名字的第一个字母转为小写(用它作为key存储map)
            String key = clazzFileName.substring(clazzFileName.lastIndexOf(PKG_SEPARATOR) + 1);
            key = StringUtils.firstLowerCase(key);

            //在上方已将文件分隔符替换成包类分隔符，可以作为全类名来获取class对象
            String className = clazzFileName;

            try {
                Class<?> clazz = Class.forName(className);
                ClassInfo classInfo = new ClassInfo(key, className, clazz);
                if(isAgree(classInfo)){
                    scanAction.action(classInfo);
                }
            } catch (ClassNotFoundException e) {
                logger.warn("扫描jar时出现无法创建对象的类", className);
            } catch(NoClassDefFoundError ignored){ }
        }
    }

    private void scanFilePackage(final String pkg) {
        String absDir = SCAN_BASE_PACKAGE + pkg;
        File absFile = new File(absDir);
        File[] absSubFiles = Objects.requireNonNull(
                absFile.listFiles(file -> {
                    String fName = file.getName();
                    if (file.isDirectory()) {
                        this.scanFilePackage(pkg + PATH_SEPARATOR + fName);
                    } else {
                        //判断文件后缀是否为.class
                        return fName.endsWith(Suffix_CLASS);
                    }
                    return false;
                })
        );

        String pkgPath = pkg.replaceAll(PATH_SEPARATOR, "\\" + PKG_SEPARATOR);

        for (File clazzFile : absSubFiles) {
            String clazzFileName = clazzFile.getName();
            //去除.class以后的文件名
            clazzFileName = clazzFileName.substring(0, clazzFileName.lastIndexOf(PKG_SEPARATOR));
            //将名字的第一个字母转为小写(用它作为key存储map)
            String key = StringUtils.firstLowerCase(clazzFileName);
            //构建一个类全名(包名.类名)
            String className = pkgPath + PKG_SEPARATOR + clazzFileName;

            try {
                Class<?> clazz = Class.forName(className);

                ClassInfo classInfo = new ClassInfo(key, className, clazz);
                if(isAgree(classInfo)){
                    scanAction.action(classInfo);
                }
            } catch (ClassNotFoundException e) {
                logger.warn("扫描Jar时出现无法创建对象的类", className);
            }
        }
    }

    private boolean isAgree(ClassInfo classInfo){
        return filter==null||filter.isAgree(classInfo);
    }
}

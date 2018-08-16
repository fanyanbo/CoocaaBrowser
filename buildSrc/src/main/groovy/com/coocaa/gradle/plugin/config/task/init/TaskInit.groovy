package com.coocaa.gradle.plugin.config.task.init

import com.alibaba.fastjson.JSONObject
import com.coocaa.gradle.plugin.builder.CCBuilderTask
import com.coocaa.gradle.plugin.builder.utils.GitUtils
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

public class TaskInit extends CCBuilderTask {
    public static class CCProject {
        String name;
        String repo;
        String branch;

        CCProject(String name) {
            this.name = name
        }

        @Override
        public String toString() {
            return "CCProject{" +
                    "name='" + name + '\'' +
                    ", repo='" + repo + '\'' +
                    ", branch='" + branch + '\'' +
                    '}';
        }
    }

    public static class CCSdks {
        String name;
        NamedDomainObjectContainer<CCProject> repos;

        CCSdks(String name) {
            this.name = name
            repos = mProject.container(CCProject)
        }

        def void repos(Closure closure) {
            repos.configure(closure)
        }

        @Override
        public String toString() {
            return "CCSdks{" +
                    "name='" + name + '\'' +
                    ", repos=" + repos +
                    '}';
        }
    }

    public static class Extension {
        public static final String NAME = "ccbuilder_init_ext"
//        Configurable<CCProject> _project;
        NamedDomainObjectContainer<CCSdks> _cc;

        public Extension(Project project) {
//            _project = project.container(CCProject)
            _cc = project.container(CCSdks)
        }
//
//        def void _project(Closure closure) {
//            _project.configure(closure)
//        }

        def void _cc(Closure closure) {
            _cc.configure(closure)
        }
    }

    private static Project mProject;

    public static final void init(Project project) {
        mProject = project
        //NamedDomainObjectContainer<Repo> repos = project.container(Repo)
        project.extensions.add(Extension.NAME, new Extension(project))
//        project.extensions.create(Extension.NAME, Extension)
        project.task(NAME, type: this)
    }

    public static class CCIniUtil {
        public static class Repository implements Serializable {
            public String name;
            public String repository;
            public String branch;
        }

        public static class CC implements Serializable {
            public String directory;
            public List<Repository> repositories;
        }

        public static class CCIni implements Serializable {
            public Repository project;
            public List<CC> cc;
        }

        public static CCIni readINI() {
            String ini = ""
            File file = new File("cc.ini")
            if (file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file))
                BufferedReader bufferedReader = new BufferedReader(read)
                String lineTxt = null
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    ini += lineTxt
                }
                read.close();
            }
            return JSONObject.parseObject(ini, CCIni.class);
        }
    }

    public static final String NAME = "ccinit"

    TaskInit() {
        super(NAME)
    }

    class GitRunnable implements Runnable {
        String repo
        String localDir
        String branch

        CountDownLatch latch = new CountDownLatch(1)

        public GitRunnable(String repo, String localDir, String branch) {
            this.repo = repo//String.copyValueOf(repo.toCharArray())
            this.localDir = localDir//String.copyValueOf(localDir.toCharArray())
            this.branch = branch//String.copyValueOf(branch.toCharArray())
        }

        @Override
        void run() {
            try {
                GitUtils.initSource(repo, localDir, branch)
            } catch (Exception e) {
                ee = e
            }
            latch.countDown()
        }
        Exception ee = null

        public Exception await() {
            latch.await()
            return ee
        }
    }

    private String ff(String f) {
        String _f = f.replace('\\', '/')
        return "if (new File(\"${_f}\").exists())\n" +
                "    apply from: \"${_f}\"\n"
    }

    @Override
    void action() {
        super.action()
        Extension ext = getExt(Extension.NAME);
//        for (CCProject r in ext._project) {
//            println r.toString()
//        }
        String cc_setting_value = ""
        List<GitRunnable> runnables = new ArrayList<>()
        String ccDir = "${project.rootDir.getAbsolutePath()}${File.separator}cc"
        CCIniUtil.CCIni ini = CCIniUtil.readINI()
        cc_setting_value = initCC(ini, ccDir, runnables, cc_setting_value)

        String localDir = "${project.rootDir.getAbsolutePath()}${File.separator}project"
        runnables.add(new GitRunnable(ini.project.repository, localDir, ini.project.branch))
        cc_setting_value += ff("project${File.separator}cc_settings.gradle")

//        for (CCSdks r in ext._cc) {
//            if (r.repos != null) {
//                for (CCProject p in r.repos) {
//                    String localDir = "${ccDir}${File.separator}${r.name}${File.separator}${p.name}"
//                    runnables.add(new GitRunnable(p.repo, localDir, p.branch))
//                    cc_setting_value += ff("${localDir}${File.separator}setting.gradle")
//                }
//            }
//            println r.toString()
//        }
        if (runnables.size() > 0) {
            ExecutorService service = Executors.newFixedThreadPool(4)
            for (GitRunnable r in runnables) {
                service.execute(r)
            }
            for (GitRunnable r in runnables) {
                def ee = r.await()
                if (ee != null)
                    throw ee
            }
        }

        String cc_setting = "${project.rootDir.getAbsolutePath()}${File.separator}cc_settings.gradle"
        File file = new File(cc_setting)
        if (file.exists())
            file.delete()
        FileWriter fw = new FileWriter(file);
        fw.write(cc_setting_value, 0, cc_setting_value.length());
        fw.flush();
    }

    private String initCC(CCIniUtil.CCIni ini, String ccDir, ArrayList<GitRunnable> runnables, String cc_setting_value) {
        for (CCIniUtil.CC cc in ini.cc) {
            String dd = "${ccDir}${File.separator}${cc.directory}"
            for (CCIniUtil.Repository r in cc.repositories) {
                String localDir = "${dd}${File.separator}${r.name}"
                runnables.add(new GitRunnable(r.repository, localDir, r.branch))

                String relativeDir = "cc${File.separator}${cc.directory}${File.separator}${r.name}"
                cc_setting_value += ff("${relativeDir}${File.separator}cc_settings.gradle")
            }
        }
        cc_setting_value
    }
}
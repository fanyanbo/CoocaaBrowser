package com.coocaa.gradle.plugin.builder.utils

import com.coocaa.gradle.plugin.utils.Logger
import com.google.common.base.Function
import com.google.common.collect.Collections2
import org.eclipse.jgit.api.CheckoutCommand
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.NetRCCredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

public class GitUtils {
    public static void initSource(String remote, String local, String branch) {
        Logger.d("GIT", "<<<<<<<start>>>>>>>initSource from ${remote}@${branch} to ${local}")
        CredentialsProvider.setDefault(new UsernamePasswordCredentialsProvider("gogs", "gogs"));
        Git git = initGit(remote, local);
        if (git == null)
            return;
        git = fetchAll(git);
        git = checkout(git, branch)
//        git = switchBranch(branch, git);
//        git = pullBranch(git);
        git.close();
        Logger.d("GIT", "[[[[[[[end]]]]]]]initSource from ${remote}@${branch} to ${local}")
    }


    private static Git checkout(Git git, String branch) {
        CheckoutCommand checkout = git.checkout();
        checkout.setName(branch);
        checkout.setStartPoint("origin/" + branch);
        checkout.setCreateBranch(!branchExistsLocally(git, "refs/heads/" + branch));
        checkout.call();
        PullCommand pullCmd = git.pull();
        pullCmd.call();
        return git;
    }

    private static boolean branchExistsLocally(Git git, String remoteBranch) {
        List<Ref> branches = git.branchList().call();
        Collection<String> branchNames = Collections2.transform(branches, new Function<Ref, String>() {
            @Override
            public String apply(Ref input) {
                return input.getName();
            }
        });
        return branchNames.contains(remoteBranch);
    }

//    private static Git switchBranch(String branch, Git git) {
//        Map<String, Ref> refMap = git.getRepository().getAllRefs()
//        for (String key : refMap.keySet()) {
//0            Logger.d("GU", "ref:${key}--${refMap.get(key).getName()}|${refMap.get(key).target.name}")
//        }
//
//        Logger.d("GU", "switchBranch:${branch}")
//        boolean isCurrentBranch = git.getRepository().getBranch().equals(branch);
//        if (!isCurrentBranch) {
//            boolean isBranchNotExistOnLocal = git.getRepository().findRef(branch) == null;
//            if (isBranchNotExistOnLocal)
//                git.branchCreate().setName(branch).setStartPoint("refs/remotes/origin/${branch}").call();
//        }
//        git.checkout().setName(branch).call();
//        return git;
//    }

    private static Git fetchAll(Git git) {
        git.fetch().call();
        return git;
    }

    private static Git initGit(String remote, String local) {
        File file = new File(local);
        if (!file.exists()) {
            return cloneRepo(remote, local);
        } else {
            try {
                return Git.open(file);
            } catch (Exception e) {
                file.delete();
                return cloneRepo(remote, local);
            }
        }
    }

    private static Git cloneRepo(String remote, String local) {
        CloneCommand command = Git.cloneRepository()
        command.setURI(remote)
        command.setDirectory(new File(local))
        return command.call()
    }
}
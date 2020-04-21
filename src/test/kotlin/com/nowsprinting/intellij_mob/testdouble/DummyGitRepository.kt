package com.nowsprinting.intellij_mob.testdouble

import com.intellij.dvcs.ignore.VcsRepositoryIgnoredFilesHolder
import com.intellij.dvcs.repo.Repository
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.GitLocalBranch
import git4idea.GitVcs
import git4idea.branch.GitBranchesCollection
import git4idea.repo.*

internal open class DummyGitRepository : GitRepository {
    override fun getRepositoryFiles(): GitRepositoryFiles {
        throw Exception("Not yet implemented")
    }

    override fun getBranches(): GitBranchesCollection {
        throw Exception("Not yet implemented")
    }

    override fun getInfo(): GitRepoInfo {
        throw Exception("Not yet implemented")
    }

    override fun toLogString(): String {
        throw Exception("Not yet implemented")
    }

    override fun getBranchTrackInfos(): MutableCollection<GitBranchTrackInfo> {
        throw Exception("Not yet implemented")
    }

    override fun getGitDir(): VirtualFile {
        throw Exception("Not yet implemented")
    }

    override fun update() {
        throw Exception("Not yet implemented")
    }

    override fun getCurrentBranch(): GitLocalBranch? {
        throw Exception("Not yet implemented")
    }

    override fun getPresentableUrl(): String {
        throw Exception("Not yet implemented")
    }

    override fun getVcs(): GitVcs {
        throw Exception("Not yet implemented")
    }

    override fun getCurrentRevision(): String? {
        throw Exception("Not yet implemented")
    }

    override fun getState(): Repository.State {
        throw Exception("Not yet implemented")
    }

    override fun getRemotes(): MutableCollection<GitRemote> {
        throw Exception("Not yet implemented")
    }

    override fun getCurrentBranchName(): String? {
        throw Exception("Not yet implemented")
    }

    override fun getConflictsHolder(): GitConflictsHolder {
        throw Exception("Not yet implemented")
    }

    override fun getIgnoredFilesHolder(): VcsRepositoryIgnoredFilesHolder {
        throw Exception("Not yet implemented")
    }

    override fun isFresh(): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getBranchTrackInfo(localBranchName: String): GitBranchTrackInfo? {
        throw Exception("Not yet implemented")
    }

    override fun isOnBranch(): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getProject(): Project {
        throw Exception("Not yet implemented")
    }

    override fun getRoot(): VirtualFile {
        throw Exception("Not yet implemented")
    }

    override fun getSubmodules(): MutableCollection<GitSubmoduleInfo> {
        throw Exception("Not yet implemented")
    }

    override fun isRebaseInProgress(): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getUntrackedFilesHolder(): GitUntrackedFilesHolder {
        throw Exception("Not yet implemented")
    }

    override fun dispose() {
        throw Exception("Not yet implemented")
    }
}
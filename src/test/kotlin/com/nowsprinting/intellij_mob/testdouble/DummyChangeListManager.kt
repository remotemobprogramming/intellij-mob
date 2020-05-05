package com.nowsprinting.intellij_mob.testdouble

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.vcs.AbstractVcs
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vcs.changes.*
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.intellij.util.ThreeState
import java.io.File

open class DummyChangeListManager : ChangeListManager() {
    override fun commitChanges(changeList: LocalChangeList, changes: MutableList<out Change>) {
        throw Exception("Not yet implemented")
    }

    override fun scheduleUpdate() {
        throw Exception("Not yet implemented")
    }

    override fun getChange(file: VirtualFile): Change? {
        throw Exception("Not yet implemented")
    }

    override fun getChange(file: FilePath?): Change? {
        throw Exception("Not yet implemented")
    }

    override fun getAffectedPaths(): MutableList<File> {
        throw Exception("Not yet implemented")
    }

    override fun getAffectedFiles(): MutableList<VirtualFile> {
        throw Exception("Not yet implemented")
    }

    override fun findChangeList(name: String?): LocalChangeList? {
        throw Exception("Not yet implemented")
    }

    override fun scheduleAutomaticEmptyChangeListDeletion(list: LocalChangeList) {
        throw Exception("Not yet implemented")
    }

    override fun scheduleAutomaticEmptyChangeListDeletion(list: LocalChangeList, silently: Boolean) {
        throw Exception("Not yet implemented")
    }

    override fun removeChangeListListener(listener: ChangeListListener) {
        throw Exception("Not yet implemented")
    }

    override fun removeChangeList(name: String) {
        throw Exception("Not yet implemented")
    }

    override fun removeChangeList(list: LocalChangeList) {
        throw Exception("Not yet implemented")
    }

    override fun isUnversioned(file: VirtualFile?): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getDefaultListName(): String {
        throw Exception("Not yet implemented")
    }

    override fun getFilesToIgnore(): Array<IgnoredFileBean> {
        throw Exception("Not yet implemented")
    }

    override fun getChangeListNameIfOnlyOne(changes: Array<out Change>?): String? {
        throw Exception("Not yet implemented")
    }

    override fun getChangesIn(dir: VirtualFile): MutableCollection<Change> {
        throw Exception("Not yet implemented")
    }

    override fun getChangesIn(path: FilePath): MutableCollection<Change> {
        throw Exception("Not yet implemented")
    }

    override fun registerCommitExecutor(executor: CommitExecutor) {
        throw Exception("Not yet implemented")
    }

    override fun reopenFiles(paths: MutableList<out FilePath>) {
        throw Exception("Not yet implemented")
    }

    override fun isIgnoredFile(file: VirtualFile): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun isIgnoredFile(file: FilePath): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun addFilesToIgnore(vararg ignoredFiles: IgnoredFileBean?) {
        throw Exception("Not yet implemented")
    }

    override fun addDirectoryToIgnoreImplicitly(path: String) {
        throw Exception("Not yet implemented")
    }

    override fun getStatus(file: VirtualFile): FileStatus {
        throw Exception("Not yet implemented")
    }

    override fun setReadOnly(name: String, value: Boolean): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun editName(fromName: String, toName: String): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun getSwitchedBranch(file: VirtualFile): String? {
        throw Exception("Not yet implemented")
    }

    override fun addChangeList(name: String, comment: String?): LocalChangeList {
        throw Exception("Not yet implemented")
    }

    override fun setFilesToIgnore(vararg ignoredFiles: IgnoredFileBean?) {
        throw Exception("Not yet implemented")
    }

    override fun isFreezed(): String? {
        throw Exception("Not yet implemented")
    }

    override fun editComment(name: String, newComment: String?): String? {
        throw Exception("Not yet implemented")
    }

    override fun invokeAfterUpdate(
        afterUpdate: Runnable,
        mode: InvokeAfterUpdateMode,
        title: String?,
        state: ModalityState?
    ) {
        throw Exception("Not yet implemented")
    }

    override fun invokeAfterUpdate(
        afterUpdate: Runnable,
        mode: InvokeAfterUpdateMode,
        title: String?,
        dirtyScopeManager: Consumer<in VcsDirtyScopeManager>?,
        state: ModalityState?
    ) {
        throw Exception("Not yet implemented")
    }

    override fun haveChangesUnder(vf: VirtualFile): ThreeState {
        throw Exception("Not yet implemented")
    }

    override fun getRegisteredExecutors(): MutableList<CommitExecutor> {
        throw Exception("Not yet implemented")
    }

    override fun getModifiedWithoutEditing(): MutableList<VirtualFile> {
        throw Exception("Not yet implemented")
    }

    override fun getChangeList(id: String?): LocalChangeList? {
        throw Exception("Not yet implemented")
    }

    override fun getChangeList(change: Change): LocalChangeList? {
        throw Exception("Not yet implemented")
    }

    override fun getChangeList(file: VirtualFile): LocalChangeList? {
        throw Exception("Not yet implemented")
    }

    override fun getVcsFor(change: Change): AbstractVcs? {
        throw Exception("Not yet implemented")
    }

    override fun getChangeLists(): MutableList<LocalChangeList> {
        throw Exception("Not yet implemented")
    }

    override fun getChangeLists(change: Change): MutableList<LocalChangeList> {
        throw Exception("Not yet implemented")
    }

    override fun getChangeLists(file: VirtualFile): MutableList<LocalChangeList> {
        throw Exception("Not yet implemented")
    }

    override fun getChangeListsNumber(): Int {
        throw Exception("Not yet implemented")
    }

    override fun addChangeListListener(listener: ChangeListListener, disposable: Disposable) {
        throw Exception("Not yet implemented")
    }

    override fun addChangeListListener(listener: ChangeListListener) {
        throw Exception("Not yet implemented")
    }

    override fun setDefaultChangeList(name: String) {
        throw Exception("Not yet implemented")
    }

    override fun setDefaultChangeList(list: LocalChangeList) {
        throw Exception("Not yet implemented")
    }

    override fun getDefaultChangeList(): LocalChangeList {
        throw Exception("Not yet implemented")
    }

    override fun isFileAffected(file: VirtualFile): Boolean {
        throw Exception("Not yet implemented")
    }

    override fun removeImplicitlyIgnoredDirectory(path: String) {
        throw Exception("Not yet implemented")
    }

    override fun moveChangesTo(list: LocalChangeList, vararg changes: Change?) {
        throw Exception("Not yet implemented")
    }

    override fun getAllChanges(): MutableCollection<Change> {
        throw Exception("Not yet implemented")
    }

    override fun isFreezedWithNotification(modalTitle: String?): Boolean {
        throw Exception("Not yet implemented")
    }
}
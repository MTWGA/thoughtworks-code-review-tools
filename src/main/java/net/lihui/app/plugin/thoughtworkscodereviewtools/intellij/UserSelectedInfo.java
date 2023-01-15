package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Optional;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class UserSelectedInfo {
    private final Project project;
    private final VirtualFile file;
    private final Editor editor;

    public UserSelectedInfo(AnActionEvent actionEvent) {
        this.project = actionEvent.getProject();
        this.file = actionEvent.getData(VIRTUAL_FILE);
        this.editor = actionEvent.getData(CommonDataKeys.EDITOR);
    }

    public Project getProject() {
        return project;
    }

    public String getProjectName() {
        return defaultString(project.getName());
    }

    public String getSelectedFilePath() {
        return Optional.ofNullable(file)
                .map(VirtualFile::getCanonicalPath)
                .map(this::getRelativePath)
                .orElse("");
    }

    public String getSelectedText() {
        return editor != null ? editor.getSelectionModel().getSelectedText() : "";
    }

    private String getRelativePath(String canonicalPath) {
        int projectNameIndex = canonicalPath.indexOf(getProjectName());
        boolean isExistedFolded = this.editor.getFoldingModel().getAllFoldRegions().length > 0;
        return canonicalPath.substring(projectNameIndex + getProjectName().length() + 1)
                + ' '
                + getSelectedTextStartLine(isExistedFolded)
                + '-'
                + getSelectTextEndLine(isExistedFolded);
    }

    private int getSelectedTextStartLine(boolean isExistedFolded) {
        VisualPosition selectionStartPosition = this.editor.getSelectionModel().getSelectionStartPosition();
        return isExistedFolded ? this.editor.visualToLogicalPosition(selectionStartPosition).line + 1 : selectionStartPosition.getLine() + 1;
    }

    private int getSelectTextEndLine(boolean isExistedFolded) {
        VisualPosition selectionEndPosition = this.editor.getSelectionModel().getSelectionEndPosition();
        return isExistedFolded ? this.editor.visualToLogicalPosition(selectionEndPosition).line + 1 : selectionEndPosition.getLine() + 1;
    }
}

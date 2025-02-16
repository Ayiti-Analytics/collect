package org.odk.collect.android.regression;

import android.Manifest;

import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.odk.collect.android.R;
import org.odk.collect.android.support.CollectTestRule;
import org.odk.collect.android.support.CopyFormRule;
import org.odk.collect.android.support.ResetStateRule;
import org.odk.collect.android.support.pages.AccessControlPage;
import org.odk.collect.android.support.pages.ProjectSettingsPage;
import org.odk.collect.android.support.pages.MainMenuPage;
import org.odk.collect.android.support.pages.ResetApplicationDialog;

//Issue NODK-240
public class ResetApplicationTest {

    public CollectTestRule rule = new CollectTestRule();

    @Rule
    public RuleChain copyFormChain = RuleChain
            .outerRule(GrantPermissionRule.grant(Manifest.permission.READ_PHONE_STATE))
            .around(new ResetStateRule())
            .around(new CopyFormRule("All_widgets.xml"))
            .around(rule);

    @Test
    public void when_rotateScreen_should_resetDialogNotDisappear() {
        //TestCase1
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .clickProjectManagement()
                .clickOnResetApplication()
                .assertText(R.string.reset_settings_dialog_title)
                .assertDisabled(R.string.reset_settings_button_reset)
                .rotateToLandscape(new ResetApplicationDialog())
                .assertText(R.string.reset_settings_dialog_title)
                .assertDisabled(R.string.reset_settings_button_reset)
                .rotateToPortrait(new ResetApplicationDialog())
                .assertText(R.string.reset_settings_dialog_title)
                .assertDisabled(R.string.reset_settings_button_reset);
    }

    @Test
    public void savedAndBlankForms_shouldBeReset() {
        //TestCase1,4
        new MainMenuPage()
                .startBlankForm("All widgets")
                .clickGoToArrow()
                .clickJumpEndButton()
                .clickSaveAndExit()
                .clickEditSavedForm()
                .assertText("All widgets")
                .pressBack(new MainMenuPage())
                .openProjectSettingsDialog()
                .clickSettings()
                .clickProjectManagement()
                .clickOnResetApplication()
                .assertDisabled(R.string.reset_settings_button_reset)
                .clickOnString(R.string.reset_saved_forms)
                .clickOnString(R.string.reset_blank_forms)
                .clickOnString(R.string.reset_settings_button_reset)
                .clickOKOnDialog();
        new MainMenuPage()
                .clickFillBlankForm()
                .assertTextDoesNotExist("All widgets")
                .pressBack(new MainMenuPage())
                .clickEditSavedForm()
                .assertTextDoesNotExist("All widgets");
    }

    @Test
    public void adminSettings_shouldBeReset() {
        //TestCase2
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .clickAccessControl()
                .openUserSettings()
                .uncheckServerOption()
                .pressBack(new AccessControlPage())
                .pressBack(new ProjectSettingsPage())
                .pressBack(new MainMenuPage())
                .openProjectSettingsDialog()
                .clickSettings()
                .checkIfServerOptionIsNotDisplayed()
                .pressBack(new MainMenuPage())
                .openProjectSettingsDialog()
                .clickSettings()
                .clickProjectManagement()
                .clickOnResetApplication()
                .clickOnString(R.string.reset_settings)
                .clickOnString(R.string.reset_settings_button_reset)
                .clickOKOnDialog();
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .checkIfServerOptionIsDisplayed();
    }

    @Test
    public void userInterfaceSettings_shouldBeReset() {
        //TestCase3
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .clickOnUserInterface()
                .assertText(R.string.theme_system)
                .clickOnTheme()
                .clickOnString(R.string.theme_dark);
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .clickOnUserInterface()
                .assertText(R.string.theme_dark)
                .clickOnLanguage()
                .clickOnSelectedLanguage("español");
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .clickOnUserInterface()
                .assertText("español")
                .pressBack(new ProjectSettingsPage())
                .pressBack(new MainMenuPage())
                .openProjectSettingsDialog()
                .clickSettings()
                .clickProjectManagement()
                .clickOnResetApplication()
                .clickOnString(R.string.reset_settings)
                .clickOnString(R.string.reset_settings_button_reset)
                .clickOKOnDialog();
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .clickOnUserInterface()
                .assertText(R.string.theme_system)
                .assertTextDoesNotExist(R.string.theme_dark)
                .assertText(R.string.use_device_language)
                .assertTextDoesNotExist("español");
    }

    @Test
    public void formManagementSettings_shouldBeReset() {
        //TestCase3
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .openFormManagement()
                .clickOnAutoSend()
                .clickOnString(R.string.wifi_autosend)
                .assertText(R.string.wifi_autosend)
                .clickOnDefaultToFinalized()
                .pressBack(new ProjectSettingsPage())
                .pressBack(new MainMenuPage())
                .startBlankForm("All widgets")
                .clickGoToArrow()
                .clickJumpEndButton()
                .assertMarkFinishedIsNotSelected()
                .clickSaveAndExit()
                .openProjectSettingsDialog()
                .clickSettings()
                .clickProjectManagement()
                .clickOnResetApplication()
                .clickOnString(R.string.reset_settings)
                .clickOnString(R.string.reset_settings_button_reset)
                .clickOKOnDialog();
        new MainMenuPage()
                .openProjectSettingsDialog()
                .clickSettings()
                .openFormManagement()
                .assertText(R.string.off)
                .pressBack(new ProjectSettingsPage())
                .pressBack(new MainMenuPage())
                .startBlankForm("All widgets")
                .clickGoToArrow()
                .clickJumpEndButton()
                .assertMarkFinishedIsSelected()
                .clickSaveAndExit();
    }

}

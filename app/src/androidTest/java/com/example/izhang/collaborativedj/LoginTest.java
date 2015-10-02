package com.example.izhang.collaborativedj;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.izhang.collaborativedj.HostActivity;
import com.example.izhang.collaborativedj.Playlist;
import com.example.izhang.collaborativedj.login;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class LoginTest
        extends ActivityUnitTestCase<login> {

    private Intent activityIntent;
    private Activity mLoginTest;
    private EditText codeText;

    private Button hostButton;
    private Button loginButton;

    public LoginTest() {
        super(login);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activityIntent = new Intent(getInstrumentation()
        .getTargetContext(), login);
        startActivity(activityIntent, null, null);
        mLoginTest = getActivity();
        loginButton = (Button) mLoginTest.findViewById(R.id.codeEdit);
        hostButton = (Button) mLoginTest.findViewById(R.id.hostButton);
        codeText =(EditText) mLoginTest.findViewById(R.id.codeEdit);

    }
    @MediumTest
    public void testEditText(){
        startActivity(activityIntent, null, null);
        final String testGood="2342342";
        final String testLong="1241412421414";
        final String testNothing="";
        codeText.setText(testGood);
        codeText.setText(testLong);
        codeText.setText(testNothing);
    }
    @LargeTest
    public void testHostButton() {
        startActivity(activityIntent, null, null);
        hostButton.performClick();

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
        assertTrue(isFinishCalled());

        final String payload =
                launchIntent.getStringExtra(NextActivity.EXTRAS_PAYLOAD_KEY);
        assertEquals("Payload is empty", LaunchActivity.STRING_PAYLOAD, payload);

    }
    @LargeTest
    public void testPlaylistButton() {
        startActivity(activityIntent, null, null);
        codeText.setText("2929292");
        loginButton.performClick();

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
        assertTrue(isFinishCalled());

        final String payload =
                launchIntent.getStringExtra(NextActivity.EXTRAS_PAYLOAD_KEY);
        assertEquals("Payload is empty", LaunchActivity.STRING_PAYLOAD, payload);

    }
}
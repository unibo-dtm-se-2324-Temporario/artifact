import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.temporario.Login.LoginFragment
import com.example.temporario.Login.MainActivity
import com.example.temporario.Login.SplashFragment
import com.example.temporario.R
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {

    @Test
    fun testSplashFragmentDuration() {
        // Launch the SplashFragment in the MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Expected splash duration in milliseconds
        val splashScreenDuration = 3000L // 3 seconds

        // Start time
        val startTime = System.currentTimeMillis()

        // Wait for the splash screen duration
        Thread.sleep(splashScreenDuration)

        // Calculate the elapsed time
        val elapsedTime = System.currentTimeMillis() - startTime
        assertTrue(elapsedTime >= splashScreenDuration)

        // Check if MainActivity is displayed after the splash duration
        onView(withId(R.id.linearLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun testSplashFragmentTransitionToLoginFragment() {
        // Launch MainActivity which contains the SplashFragment
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Wait for the splash screen transition to complete
        Thread.sleep(3000)

        // Verify that the LoginFragment is now displayed
        activityScenario.onActivity { activity ->
            val loginFragment = activity.supportFragmentManager.findFragmentById(R.id.fragment_container) as LoginFragment?
            assertNotNull(loginFragment)
        }

        // Check if a view in the LoginFragment is displayed
        onView(withId(R.id.login_fragment_container))
            .check(matches(isDisplayed()))
    }


}

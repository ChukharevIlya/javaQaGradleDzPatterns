package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.security.Key;
import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
public class AppCardDeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = false;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $x("//*[@data-test-id=\"city\"] //*[@placeholder=\"Город\"]").setValue(validUser.getCity());
        $x("//*[@data-test-id=\"date\"] //*[@placeholder=\"Дата встречи\"]").setValue(firstMeetingDate);
        $x("//*[@data-test-id=\"name\"] //*[@name=\"name\"]").setValue(validUser.getName());
        $x("//*[@data-test-id=\"phone\"] //*[@name=\"phone\"]").setValue(validUser.getPhone());
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//*[@type=\"button\"]//span[contains(text(),\"Запланировать\")]").click();
        $x("//*[@data-test-id=\"success-notification\"]").should(Condition.visible, Duration.ofSeconds(15));
        $x("//*[@data-test-id=\"date\"] //*[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//*[@data-test-id=\"date\"] //*[@placeholder=\"Дата встречи\"]").setValue(secondMeetingDate);
        $x("//*[@type=\"button\"]//span[contains(text(),\"Запланировать\")]").click();
        $x("//*[@data-test-id=\"replan-notification\"]//span[text()=\"Перепланировать\"]").click();
        $x("//*[@data-test-id=\"success-notification\"]//div[contains(text(),\"Встреча успешно запланирована на \")]").shouldBe(Condition.visible).shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}

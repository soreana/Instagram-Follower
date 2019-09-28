# Instagram Follower
This project aimed to provide api for Instagram functionality like follow and unfollow. It has login, follow and unfollow functionality.
 
 To run this project you should:
 * Download Selenium: curl -O http://selenium-release.storage.googleapis.com/3.7/selenium-server-standalone-3.7.1.jar
 * Download geckodriver:
    * Linux: curl -L https://github.com/mozilla/geckodriver/releases/download/v0.11.1/geckodriver-v0.11.1-linux64.tar.gz | tar xz
    * Mac OS: curl -L https://github.com/mozilla/geckodriver/releases/download/v0.11.1/geckodriver-v0.11.1-macos.tar.gz | tar xz
 * Run Selenium server: java -jar -Dwebdriver.geckodriver.driver=./geckodriver selenium-server-standalone-3.7.1.jar

### maintenance notice
My friends follow and unfollow users in Instagram to increase their followers.
 This project started to do that job automatically and provide some api to 
 talk with Instagram. At the end I find [this](https://github.com/brunocvcunha/instagram4j) 
 project that can do what ever you need with Instagram. As a result 
 I will not maintain this repository any more :)

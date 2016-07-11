#CurrencyCalc a Dagger 2 + MVP + Retrofit 2 + Rx + Realm Example

This app is made as an example of how to use Dagger 2 together with MVP. The app also uses Realm and Retrofit 2 in combination with RxJava. You are welcome to check it out and use the code.

The application consists of three screens: 

- Currency calculator - this screen allows you to calculate Croatian Kuna to another currency and vice versa. 
- Exchange rates - displays the current exchange rates from the HNB API. For every currency buying rate, median rate and selling rate (depending on the Kuna) are shown.
- Currency stats - statistical preview for last 7 days of a currency exchange rate depending on the Kuna.

The package strucutre explained:

- adapters - adapter classes
- internals - dagger and mvp stuff
- networking - retrofit apis
- realm - all the realm object classes
- support - all the utils, helpers, managers and everything else that I think is a support class. You can also divide it into several package names.
- ui - I normally use the ui package for dialogs, activities, fragmetns. Inside the package I usually create an additional package name for every major feature of the app. 

#TODO

- Write tests
- Fix slow initial start time of the app

#Additional libs used

- [Butterknife](http://jakewharton.github.io/butterknife/)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- [JodaTimeAndroid](https://github.com/dlew/joda-time-android)

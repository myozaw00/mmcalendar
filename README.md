

![screen](https://raw.githubusercontent.com/myozaw00/mmcalendar/development/images/screen_shot_05.png)

## Usage

`DatePickerDialogFragment`extends `DialogFragment`, for example:

```
val fromDate = Calendar.getInstance()
fromDate.set(1950, 0, 1)
val toDate = Calendar.getInstance()
toDate.set(2030, 0, 1)

val datePicker = DatePickerDialogFragment(this)
val ft = supportFragmentManager.beginTransaction()
val prev = supportFragmentManager.findFragmentByTag("dialog")
if (prev != null)
	{
            ft.remove(prev)
        }
ft.addToBackStack(null)
datePicker.isCancelable = true
datePicker.show(ft, "dialog")
```


## Installation

DatePicker is installed by adding the following dependency to your `build.gradle` file:

```groovy

allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	implementation 'com.github.myozaw00:mmcalendar:1.2.0'
}
```






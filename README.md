android-skeleton-project
========================

This is the Android Skeleton Project that I use when I start a new project.
It contains various utilities, fixes, bolierplate code and stuff that I use regularly and have developed over 5 years of working with Android.
It's not meant to be run on the device, so don't try that. It's just a basic architecture with a set of some neat abstractions.

If you will be using this skeleton application for your development, I will be greatful if you could send me a 'thanks' in the way you believe satisfying=)

Here's a list of things that are in the skeleton:
:: architecture that supports both phones and tablets
  - settings.xml in values-sw600dp, values-xlarge and values folders describes if it's a tablet or not

:: all activites go from FragmentActivity with latest Support Library

:: BaseContext implementation available in all Activites and Fragments
  - stores some utility classes, database access, network layer

:: abstract BaseActivity implementation with lots of bolierplate utilites
  - tracking activity state allowing to check it in asyncronous requests 
  - basic LoadingOverlay implementation
  - stores Handler to allow posting to UI thread when necessary
  - adds MixPanel and BugSense implementation
  - adds restart method
  - adds generic error dialogs
  - allows to share stored logcat messages
  - adds event debugging for MixPanel and other analytics
  - leaves breadcrumbs for Bugsense

:: abstract BaseFragment implementation
  - Leaves breadcrumbs for Bugsenes
  - stores context
  - solves contentView issue when Fragment.getView() returns support library view wrapper instead of root view

:: abstract BaseDialogFragment implementation
  - similar to BaseFragment
  - applies floating dialog theme
  - solves multiple displaying issues

:: Extended Views
  - WrappedCheckBox - Checkbox wrapped in LinearLayout
  - ExtendedTextView - TextView that knows how to fit text width
  - LoadingOverlay - a basic implementaion to make nice loading overlays
  - BasePopupDialog - base dialog with some basic abilities

:: StaticCache - just a static cache for all project to access it when needed (use carefully)

:: DataManager - class that is usually responsible in initing database and do stuff on data

:: AbstractDataLoader - a neat abstraction on loaders that abstracts away all complex stuff exposing only simple buildList() method - don't recall where I grabbed it

:: Network Layer
  - Retrofit + okHTTP + Jackson combination I use in my projects
  - Jackson converters and helpers
  - Basic Retrofit NetworkManager with caching and timeouts
  - stub for Retorift RestService
  - GenericCallback implementation for Retrofit

:: Data Layer
  - SharedPreferencesCache abstraction on top of generic preferences cache
  - PermanentCache sample that I use in many projects, uses SharedPreferencesCache abtraction leaving only save(), restore() and reset() to implement
:: Amazing SQLiteTable abstraction that abstracts away necessity to remember column indexes. With BasicModel conjunction makes database layer neat and easy to maintain
  - SQLiteDb class to store actual database logic
  - BaseSQLiteDB class that has database helper and database initialization logic
:: BasicModel + DBInterface abstraction that allows to tie Model with database table, adding all database logic into the model class allowing to have everything model-database-related to be in one place.

:: Utility classes
  - FontUtils - a simple utility that allows to use custom fonts. Can be used with conjunction to ExtendedTextView if necessary to make custom font text views (send me a message if you need an example)
  - IntentParams - I move all params for all Intents into this interface - adds a lot to code readability
  - LoadingManager - an abstraction that if used properly can simpify doing several network request and waiting for thier results on app initialization
  - Logger - just a neat abstraction I use on top of default Log class
  - OnDoneCallback - a generic callback for asyncronous request when you don't want to add another one interface/listener 
  - TimeUtils - a utility on top of SimpleDateFormat abstracting some logic away
  - Utils - GlobalLayoutListener abstractions, rounding, setText for any object, initOrRestartLoader, AsyncTasks fixes

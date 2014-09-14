GridImageSearch
===============

This is an Android demo application for using the Google image search API and viewing the results in an infinite scrolling grid view. 

Time spent: 5 hours spent in total

Completed user stories:
 
  * [x]User can enter a search query that will display a grid of image results from the Google Image API.
  * [x]User can click on "settings" which allows selection of advanced search options to filter results
  * [x]User can configure advanced search filters such as: 
      Size (small, medium, large, extra-large)
      Color filter (black, blue, brown, gray, green, etc...)
      Type (faces, photo, clip art, line art)
      Site (espn.com)
  * [x]Subsequent searches will have any filters applied to the search results
  * [x]User can tap on any image in results to see the image full-screen
  * [x]User can scroll down “infinitely” to continue loading more image results (up to 8 pages)
 
 
Notes:
There are a few fixes needed:
  * [x]Typing return in the search query text field crashes the app.
  * [x]It would be better to show a spinner in the image view while the image loads. 
 * [x]And when the view is being reused, clear the image right away, right now it switches from the old image to the new one.

Walkthrough of all user stories:

![Video Walkthrough](gridImageSearch.gif)

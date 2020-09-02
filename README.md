Dual list selector is a highly customizable and easy to use Android input view to select multiple options from a list of items. This widget (which allows the users to move items between two lists) is well known for the [**Web platform**](https://www.jqueryscript.net/blog/best-dual-list-box.html) but when we try to apply it on Mobile it is simply unusable in it's original form. Dual list selector rethinks and redesign this concept and provides a new alternative for selector components.

TODO VIDEO

## Table of Contents
1. [Setup](#setup)
1. [Usage](#usage)
1. [Listeners](#listeners)
1. [Plans](#plans)

## Setup
Add it to your project level (root) build.gradle file:
```gradle
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```
Add it to your module level (app) build.gradle file:
```gradle
dependencies {
    implementation 'com.github.vardroidk:dual-list-selector:1.0.0'
}
```
## Usage
### 1. Define layout
Define a `DualListSelectorView` in xml layout (all `app:dls_*` attributes are optional):

```xml
<com.vardroid.duallistselector.duallist.DualListSelectorView
  android:id="@+id/dual_list_selector_view"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  app:dls_list_background_color="#EDEFCF"
  app:dls_show_title="true"
  app:dls_title_size="18sp"
  app:dls_title_color="#595775"
  app:dls_selectable_list_title_text="Best movies"
  app:dls_selected_list_title_text="My bucket list"
  app:dls_show_empty_list_text="true"
  app:dls_selectable_empty_list_text="No more movies to select.
  app:dls_selected_empty_list_text="No movie selected yet..."
  app:dls_show_list_item_animation="true"
  app:dls_scroll_to_inserted_list_item="false"
  app:dls_selected_list_item_insertion_order="end"
  app:dls_show_list_item_divider="true"
  app:dls_list_item_divider_height="1dp"
  app:dls_list_item_divider_color="#595775"
  app:dls_list_item_image_size="60dp"
  app:dls_list_item_primary_text_size="16sp"
  app:dls_list_item_primary_text_color="#728CA3"
  app:dls_list_item_secondary_text_size="14sp"
  app:dls_list_item_secondary_text_color="#8C7462"
  app:dls_divider_background_color="#595775"
  app:dls_divider_handlebar_color="#EDEFCF" />
 ```
 
 ### 2. Provide data
 The domain objects you want to make selectable must be converted to `SelectorListItem` objects:
 ```java
List<Movie> movies = MovieProvider.getInstance().getMovies(this);
List<SelectorListItem> items = ConverterUtils.convertList(
        movies,
        movie -> SelectorListItem.builder()
                .id(String.valueOf(movie.getId()))                              // Mandatory, must be unique
                .primaryText(movie.getTitle())                                  // Mandatory, non-null
                .secondaryText(movie.getYear() + " - " + movie.getDirector())   // Optional, nullable
                .imageLoader((imageView, defaultImageHolder) ->                 // Optional, nullable, default (letter) image can be used as placeholder
                        Glide                                                   // Glide is just an example, any image loading implementation can be used
                                .with(SampleSingleActivity.this)
                                .load(movie.getPosterImageUrl())
                                .placeholder(defaultImageHolder.get())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView))
                .build()
);
 ```
 Then the converted item list can be set to the view:
 ```java
dualListSelectorView.setItems(items);
 ```
 OR
  ```java
dualListSelectorView.setItems(selectableItems, selectedItems);
 ```
 
### 3. Get items
Get selected items:
 ```java
List<SelectorListItem> selectedItems = dualListSelectorView.getSelectedItems();
 ```
Get selectable items:
 ```java
List<SelectorListItem> selectableItems = dualListSelectorView.getSelectableItems();
 ```
## Listeners
Use `SelectionChangeListener` to act on item selection/deselection:
 ```java
dualListSelectorView.setSelectionChangeListener(new DualListSelectorView.SelectionChangeListener() {
    @Override
    public void onItemSelected(SelectorListItem item) {
        showMessage("'" + item.getPrimaryText() + "' is selected");
    }

    @Override
    public void onItemDeselected(SelectorListItem item) {
        showMessage("'" + item.getPrimaryText() + "' is removed from selection");
    }
});
 ```
Use `SnapChangeListener` to act on snapping selectable/selection list:
 ```java
dualListSelectorView.setSnapChangeListener(new DualListSelectorView.SnapChangeListener() {
    @Override
    public void onSelectableListSnapped() {
        showMessage("Selectable (left) list is snapped");
    }

    @Override
    public void onSelectedListSnapped() {
        showMessage("Selected (right) list is snapped");
    }
});
 ```
 
 ## Plans
 * Searchable lists
 * `All` button for both lists
 * More stylish empty list indicator
 * dls_list_item_padding attribute
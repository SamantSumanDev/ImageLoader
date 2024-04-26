
# ImageLoader Android App (Without using thirdyparty library)

This Android application efficiently loads and displays images in a scrollable grid without using any third-party image loading library. It implements lazy loading of images, caching mechanisms for both memory and disk, graceful error handling for network and image loading failures, and ensures smooth scrolling performance.


## Sample

<img src="screenshot_sample.jpg" alt="App Sample Image" width="450" height="750">



## Features

- Displays images in a 3-column square grid.
- Implements lazy loading of images to enhance performance.
- Utilizes a caching mechanism to store images in both memory and disk cache for efficient retrieval.
- Gracefully handles network errors and image loading failures, providing informative error messages or placeholders.
- Supports scrolling through at least 100 images seamlessly without any lag.

## Technologies Used

- **Android SDK**: Development platform for building the Android app.
- **Kotlin**: Programming language used for app development.
- **Native Technology**: Implemented using native Android components without relying on any third-party image loading library.
- **RecyclerView**: Used to display a scrollable grid of images efficiently.
- **Asynchronous Image Loading**: Implemented using custom AsyncTask to load images asynchronously.
- **Memory Cache (LRU Cache)**: Utilized for caching images in memory to improve performance.
- **Disk Cache (File I/O)**: Implemented for caching images on disk and updating memory cache accordingly.
- **Network Connectivity Handling**: Ensures graceful handling of network errors during image loading.

## How to Use

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the project on an Android emulator or physical device.
4. Scroll through the grid of images to see lazy loading and caching mechanisms in action.
5. Ensure a stable internet connection for fetching images from the provided API.

## Performance Notes

- Lazy loading of images ensures that only the images visible on the screen are loaded, optimizing memory usage and reducing unnecessary network requests.
- Image loading for previous pages is cancelled when quickly scrolling to new pages, preventing unnecessary resource consumption.
- Smooth scrolling performance is maintained even when scrolling through a large number of images, ensuring a seamless user experience.

## Contributing

Contributions are welcome! Feel free to fork the repository, make your changes, and submit a pull request. For major changes, please open an issue first to discuss the proposed changes.

## Credits

This project is maintained by [Samant Suman](https://github.com/SamantSumanDev).

## License

This project is licensed under the [MIT License](LICENSE).

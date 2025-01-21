// Import all .js files from the current directory and subdirectories
const requireModules = require.context("./", true, /\.js$/);

// Import each module dynamically
requireModules.keys().forEach((key) => {
  if (key !== "./index.js") {
    requireModules(key); // index.js 제외
  }
});

// Import each module dynamically
//requireModules.keys().forEach((key) => {
//  // Exclude index.js, components/, and pages/
//  if (
//    key !== "./index.js" && // Exclude index.js
//    !key.startsWith("./components/") && // Exclude components/ directory
//    !key.startsWith("./pages/") // Exclude pages/ directory
//  ) {
//    requireModules(key);
//    console.log(`Module imported: ${key}`); // Optional logging for debugging
//  }
//});

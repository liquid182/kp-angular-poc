'use strict';

let path = require('path');

module.exports = {
  path: path.join(process.cwd(), 'dist-jit'),
  filename: '[name].bundle.js'
};

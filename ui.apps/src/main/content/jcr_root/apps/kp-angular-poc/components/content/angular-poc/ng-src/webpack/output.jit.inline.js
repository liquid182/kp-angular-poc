'use strict';

let path = require('path');

module.exports = {
  path: path.join(process.cwd(), 'dist-jit-inline'),
  filename: '[name].bundle.js'
};

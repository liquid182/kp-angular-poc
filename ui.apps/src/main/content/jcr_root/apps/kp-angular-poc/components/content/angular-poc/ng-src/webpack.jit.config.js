'use strict';
let path = require('path');

module.exports = {
  entry: require('./webpack/entry.jit'),

  context: path.join(process.cwd(), 'src'),

  output: require('./webpack/output.jit'),

  module: require('./webpack/module.async'),

  plugins: require('./webpack/plugins'),

  resolve: require('./webpack/resolve'),

  devServer: require('./webpack/dev-server'),

  stats: 'errors-only',

  devtool: 'source-map'
};

'use strict';

let path = require('path');
let ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
  rules: [
    {
      test: /\.ts$/,
      use: ['awesome-typescript-loader', 'angular2-template-loader?keepUrl=false'],
      exclude: [/\.(spec|e2e)\.ts$/]
    },
    {
      test: /\.html$/,
      use: 'raw-loader',
    },
    {
      test: /\.css$/,
      include: path.resolve(process.cwd(), 'src', 'app'),
      loaders: ['to-string-loader', 'css-loader'],
      exclude: /\.async\.css/
    },
    {
      test: /\.css$/,
      exclude: path.resolve(process.cwd(), 'src', 'app'),
      loader: ExtractTextPlugin.extract({
        fallback: 'style-loader',
        use: 'css-loader'
      })
    }
  ]
};

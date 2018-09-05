var path = require('path');
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

//noinspection JSUnusedGlobalSymbols
module.exports = {
    entry: {test: './index.js', 'debugger': './debugger.js'},
    resolve: {
        modules: ['./node_modules', '../build/kotlin-javascript-dependencies', '../build/classes/main'].map(function (s) {
            return path.resolve(__dirname, s);
        })
    },
    output: {
        path: __dirname + "/build",
        filename: '[name].js'
    },
    devServer: {
        hot: true
    },
    plugins: [
        new HtmlWebpackPlugin({ chunks: ['test'], template: './index.ejs'}),
        new HtmlWebpackPlugin({ chunks: ['debugger'], filename: 'debugger.html'}),
        new webpack.HotModuleReplacementPlugin()
    ]
};

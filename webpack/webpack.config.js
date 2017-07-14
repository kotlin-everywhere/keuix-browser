var path = require('path');
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: './index.js',
    resolve: {
        modules: ['./node_modules', '../build/classes/main/lib', '../build/classes/main'].map(function (s) {
            return path.resolve(__dirname, s);
        })
    },
    output: {
        path: __dirname + "/build",
        filename: "bundle.js"
    },
    devServer: {
        hot: true
    },
    plugins: [
        new HtmlWebpackPlugin({template: './index.ejs'}),
        new webpack.HotModuleReplacementPlugin()
    ]
};

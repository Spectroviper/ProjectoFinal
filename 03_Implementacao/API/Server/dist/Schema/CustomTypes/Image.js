"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CustomImage = void 0;
const graphql_1 = require("graphql");
const ImageScalarConfig = {
    name: 'Image',
    description: 'Custom scalar type for representing images',
    serialize(value) {
        return value; // Assuming the image data is already serialized as binary
    },
    parseValue(value) {
        return value; // Assuming the image data is already parsed as binary
    },
    parseLiteral(ast) {
        if (ast.kind === graphql_1.Kind.STRING) {
            // Assuming the image data is provided as a base64-encoded string
            return Buffer.from(ast.value, 'base64');
        }
        return null; // Invalid input or null value
    },
};
exports.CustomImage = new graphql_1.GraphQLScalarType(ImageScalarConfig);

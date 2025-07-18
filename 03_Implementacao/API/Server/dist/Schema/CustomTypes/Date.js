"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CustomDate = void 0;
const graphql_1 = require("graphql");
exports.CustomDate = new graphql_1.GraphQLScalarType({
    name: 'Date',
    description: 'Custom scalar type for representing dates',
    parseValue(value) {
        if (typeof value === 'string') {
            return new Date(value);
        }
        if (value === null || value === undefined) {
            return null;
        }
        return null;
    },
    serialize(value) {
        if (value === null || value === undefined) {
            return null;
        }
        return value.toISOString();
    },
    parseLiteral(ast) {
        if (ast.kind === graphql_1.Kind.NULL) {
            return null;
        }
        if (ast.kind === graphql_1.Kind.STRING) {
            return new Date(ast.value);
        }
        return null;
    },
});

import { GraphQLScalarType, Kind } from 'graphql';

export const CustomDate = new GraphQLScalarType({
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
    return (value as Date).toISOString();
  },
  parseLiteral(ast) {
    if (ast.kind === Kind.NULL) {
      return null;
  }
    if (ast.kind === Kind.STRING) {
      return new Date(ast.value);
    }
    return null;
  },
});

  
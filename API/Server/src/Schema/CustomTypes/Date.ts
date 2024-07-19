import { GraphQLScalarType, Kind } from 'graphql';

export const CustomDate = new GraphQLScalarType({
  name: 'Date',
  description: 'Custom scalar type for representing dates',
  parseValue(value) {
    if (typeof value === 'string') {
        // Convert the incoming value to a Date object if possible
        return new Date(value);
    }
    return null;
  },
  serialize(value) {
    // Convert the Date object to a string for output
    return (value as Date).toISOString();
  },
  parseLiteral(ast) {
    if (ast.kind === Kind.STRING) {
      // Convert the string literal to a Date object if possible
      return new Date(ast.value);
    }
    return null; // Invalid input
  },
});

  
import { GraphQLScalarType, Kind } from 'graphql';
import { GraphQLScalarTypeConfig } from 'graphql/type/definition';

const ImageScalarConfig: GraphQLScalarTypeConfig<any, any> = {
  name: 'Image',
  description: 'Custom scalar type for representing images',
  serialize(value) {
    return value; // Assuming the image data is already serialized as binary
  },
  parseValue(value) {
    return value; // Assuming the image data is already parsed as binary
  },
  parseLiteral(ast) {
    if (ast.kind === Kind.STRING) {
      // Assuming the image data is provided as a base64-encoded string
      return Buffer.from(ast.value, 'base64');
    }
    return null; // Invalid input or null value
  },
};

export const CustomImage = new GraphQLScalarType(ImageScalarConfig);

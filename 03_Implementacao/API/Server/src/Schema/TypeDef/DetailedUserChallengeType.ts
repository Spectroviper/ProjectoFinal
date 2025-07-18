import { GraphQLList, GraphQLObjectType } from "graphql";
import { UserChallengeType } from "./UserChallengeType";
import { UserChallengeAchievementType } from "./UserChallengeAchievementType";

export const DetailedUserChallengeType = new GraphQLObjectType({
  name: "DetailedUserChallenge",
  fields: () => ({
    UserChallenge: { type: UserChallengeType },
    UserChallengeAchievements: { type: new GraphQLList(UserChallengeAchievementType) },
  })
});
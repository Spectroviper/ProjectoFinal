import { GraphQLID, GraphQLObjectType } from "graphql";
import { ChallengeAchievements } from "../../Entities/ChallengeAchievements";
import { AchievementType } from "./AchievementType";
import { ChallengeType } from "./ChallengeType";

export const ChallengeAchievementType: GraphQLObjectType<ChallengeAchievements> = new GraphQLObjectType({
    name: "ChallengeAchievement",
    fields: () => ({
        ChallengeAchievementId: { type: GraphQLID },
        Achievement: { type: AchievementType },
        Challenge: { type: ChallengeType }
    })
})
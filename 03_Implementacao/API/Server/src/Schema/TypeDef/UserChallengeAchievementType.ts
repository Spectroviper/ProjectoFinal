import { GraphQLBoolean, GraphQLID, GraphQLInt, GraphQLObjectType } from "graphql";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";
import { CustomDate } from "../CustomTypes/Date";
import { AchievementType } from "./AchievementType";
import { ChallengeType } from "./ChallengeType";
import { UserType } from "./UserType";

export const UserChallengeAchievementType: GraphQLObjectType<UserChallengeAchievements> = new GraphQLObjectType({
    name: "UserChallengeAchievement",
    fields: () => ({
        UserChallengeAchievementId: { type: GraphQLID },
        Clear: { type: GraphQLBoolean },
        UnlockDate: { type: CustomDate },
        TotalCollected: { type: GraphQLInt },
        Achievement: { type: AchievementType },
        User: { type: UserType },
        Challenge: { type: ChallengeType }
    })
})
import { GraphQLBoolean, GraphQLID, GraphQLInt, GraphQLObjectType } from "graphql";
import { UserAchievements } from "../../Entities/UserAchievements";
import { CustomDate } from "../CustomTypes/Date";
import { AchievementType } from "./AchievementType";
import { UserType } from "./UserType";

export const UserAchievementType: GraphQLObjectType<UserAchievements> = new GraphQLObjectType({
    name: "UserAchievement",
    fields: () => ({
        UserAchievementId: { type: GraphQLID },
        Clear: { type: GraphQLBoolean },
        UnlockDate: { type: CustomDate },
        TotalCollected: { type: GraphQLInt },
        Achievement: { type: AchievementType },
        User: { type: UserType }
    })
})
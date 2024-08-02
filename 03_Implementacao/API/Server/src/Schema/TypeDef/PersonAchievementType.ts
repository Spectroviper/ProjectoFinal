import { GraphQLBoolean, GraphQLID, GraphQLInt, GraphQLObjectType } from "graphql";
import { PersonAchievements } from "../../Entities/PersonAchievements";
import { CustomDate } from "../CustomTypes/Date";
import { CustomImage } from "../CustomTypes/Image";
import { AchievementType } from "./Achievement";
import { PersonType } from "./Person";

export const PersonAchievementType: GraphQLObjectType<PersonAchievements> = new GraphQLObjectType({
    name: "PersonAchievement",
    fields: () => ({
        id: { type: GraphQLID },
        Clear: { type: GraphQLBoolean },
        UnlockDate: { type: CustomDate },
        TotalCollected: { type: GraphQLInt },
        UnlockedImage: { type: CustomImage },
        Achievement: { type: AchievementType },
        Person: { type: PersonType }
    })
})
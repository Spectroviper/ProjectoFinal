import { GraphQLList, GraphQLString } from "graphql";
import { Achievements } from "../../Entities/Achievements";
import { AchievementType } from "../TypeDef/Achievement";

export const GET_ALL_ACHIEVEMENTS = {
    type: new GraphQLList(AchievementType),
    resolve() {
        return Achievements.find();
    }
}

export const GET_ACHIEVEMENT = {
    type: AchievementType,
    args:{
        AchievementName: { type: GraphQLString },
    },
    async resolve(parent: any, args: any) {
        const {AchievementName} = args;
        const achievement = await Achievements.findOne({where: {AchievementName: AchievementName}, relations: ['Game']});
        const achievementAchievementName = achievement?.AchievementName;

        if(achievementAchievementName === AchievementName) {
            return achievement;
        }
        else{
            throw new Error("ACHIEVEMENT DOES NOT HAVE THE RIGHT ACHIEVEMENT_NAME!");
        }
    }
}


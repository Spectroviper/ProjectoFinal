import { GraphQLID, GraphQLInt, GraphQLString } from "graphql";
import { Achievements } from "../../Entities/Achievements";
import { Games } from "../../Entities/Games";
import { CustomImage } from "../CustomTypes/Image";
import { MessageType } from "../TypeDef/Messages";

export const CREATE_ACHIEVEMENT = {
    type: MessageType,
    args: {
        AchievementName: { type: GraphQLString },
        About: { type: GraphQLString },
        RetroPoints: { type: GraphQLInt },
        TotalCollectable: { type: GraphQLInt },
        Image: { type: GraphQLString },
        GameID: { type: GraphQLID }
    },
    async resolve(parent: any, args: any) {
        const { AchievementName,
            About,
            RetroPoints,
            TotalCollectable,
            Image,
            GameID} = args;
        const game = await Games.findOne({where: {id: GameID}});
        const gameGameID = game?.id;

        if(game){
            if (GameID == gameGameID) {
                const newAchievement = new Achievements();
                newAchievement.AchievementName = AchievementName;
                newAchievement.About = About;
                newAchievement.RetroPoints = RetroPoints;
                newAchievement.TotalCollectable = TotalCollectable;
                newAchievement.Image = Image;
                newAchievement.Game = game;
                await Achievements.insert(newAchievement);
                return {successful: true, message: "ACHIEVEMENT CREATED SUCCESSFULLY"}
            }
            else{
                throw new Error("ACHIEVEMENT DOES NOT HAVE THE RIGHT ACHIEVEMENT_NAME!");
            }
        }
        else{
            throw new Error("COULD NOT FIND REQUESTED GAME");
        }
        
        
    }
};

export const UPDATE_ACHIEVEMENT = {
    type: MessageType,
    args: {
        AchievementName: { type: GraphQLString },
        NewAchievementName: { type: GraphQLString},
        NewAbout: { type: GraphQLString },
        NewRetroPoints: { type: GraphQLInt },
        NewTotalCollectable: { type: GraphQLInt },
        NewImage: { type: GraphQLString },
        NewGameID: { type: GraphQLID }
        
    },
    async resolve(parent: any, args: any) {
        const {AchievementName, NewAchievementName, NewAbout, NewRetroPoints, NewTotalCollectable, NewImage, NewGameID} = args
        const achievement = await Achievements.findOne({where: {AchievementName: AchievementName}})
        const achievementAchievementName = achievement?.AchievementName;

        if (AchievementName === achievementAchievementName) {
            await Achievements.update({AchievementName: AchievementName},{AchievementName: NewAchievementName, About: NewAbout, RetroPoints: NewRetroPoints, TotalCollectable: NewTotalCollectable, Image: NewImage, Game: NewGameID});
            return {successful: true, message: "ACHIEVEMENT UPDATED SUCCESSFULLY"}
        } else{
            throw new Error("ACHIEVEMENT DOES NOT HAVE THE RIGHT ACHIEVEMENT_NAME!");
        }
    }
};

export const DELETE_ACHIEVEMENT = {
    type: MessageType,
    args: {
        id: { type: GraphQLID},
    },
    async resolve(parent: any, args: any) {
        const id = args.id;
        await Achievements.delete(id)
        return {successful: true, message: "ACHIEVEMENT DELEATED SUCCESFULLY"}
    }
};
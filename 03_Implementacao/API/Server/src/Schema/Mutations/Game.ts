import { GraphQLID, GraphQLString } from "graphql";
import { Games } from "../../Entities/Games";
import { CustomDate } from "../CustomTypes/Date";
import { CustomImage } from "../CustomTypes/Image";
import { MessageType } from "../TypeDef/Messages";

export const CREATE_GAME = {
    type: MessageType,
    args: {
        GameName: { type: GraphQLString },
        About: { type: GraphQLString },
        Console: { type: GraphQLString},
        Developer: { type: GraphQLString },
        Publisher: { type: GraphQLString },
        Genre: {  type: GraphQLString },
        ReleaseDate: { type: CustomDate},
        Image: { type: GraphQLString }
    },
    async resolve(parent: any, args: any) {
        const { GameName,
            About,
            Console,
            Developer,
            Publisher,
            Genre,
            ReleaseDate,
            Image} = args;
        await Games.insert(args)
        return {successful: true, message: "GAME CREATED SUCCESSFULLY"}
    }
};

export const UPDATE_GAME = {
    type: MessageType,
    args: {
        GameName: { type: GraphQLString },
        NewGameName: { type: GraphQLString},
        NewAbout: { type: GraphQLString },
        NewConsole: { type: GraphQLString },
        NewDeveloper: { type: GraphQLString },
        NewPublisher: { type: GraphQLString },
        NewGenre: { type: GraphQLString },
        NewReleaseDate: { type: CustomDate},
        NewImage: { type: GraphQLString}
        
    },
    async resolve(parent: any, args: any) {
        const {GameName, NewGameName, NewAbout, NewConsole, NewDeveloper, NewPublisher, NewGenre, NewReleaseDate, NewImage} = args
        const game = await Games.findOne({where: {GameName: GameName}})
        const gameGameName = game?.GameName;

        if (GameName === gameGameName) {
            await Games.update({GameName: GameName},{GameName: NewGameName, About: NewAbout, Console: NewConsole, Developer: NewDeveloper, Publisher: NewPublisher, Genre: NewGenre, ReleaseDate: NewReleaseDate, Image: NewImage});
            return {successful: true, message: "GAME UPDATED SUCCESSFULLY"}
        } else{
            throw new Error("GAME DOES NOT HAVE THE RIGHT GAME_NAME!");
        }
    }
};

export const DELETE_GAME = {
    type: MessageType,
    args: {
        id: { type: GraphQLID},
    },
    async resolve(parent: any, args: any) {
        const id = args.id;
        await Games.delete(id)
        return {successful: true, message: "GAME DELEATED SUCCESFULLY"}
    }
};
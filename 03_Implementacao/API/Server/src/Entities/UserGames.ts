import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, Unique } from "typeorm";
import { Games } from "./Games";
import { Users } from "./Users";

@Entity()
@Unique(['User', 'Game'])
export class UserGames extends BaseEntity {

    @PrimaryGeneratedColumn()
    UserGameId!: number;

    @Column({ type: 'float', default: 0 })
    AverageCompletion!: number;

    @ManyToOne(() => Users, users => users.UserGames)
    @JoinColumn({ name: 'UserId' })
    User!: Users;

    @ManyToOne(() => Games, games => games.UserGames)
    @JoinColumn({ name: 'GameId' })
    Game!: Games;

}
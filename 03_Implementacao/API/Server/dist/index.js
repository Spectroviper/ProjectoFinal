"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const server_1 = require("@apollo/server");
const express4_1 = require("@apollo/server/express4");
const cors_1 = __importDefault(require("cors"));
const path_1 = __importDefault(require("path"));
const Schema_1 = require("./Schema");
const upload_1 = require("./middleware/upload");
const DataSource_1 = require("./DataSource");
const main = () => __awaiter(void 0, void 0, void 0, function* () {
    try {
        yield DataSource_1.appDataSource.initialize();
        console.log('Data Source has been initialized!');
        const app = (0, express_1.default)();
        app.use((0, cors_1.default)());
        app.use(express_1.default.json());
        app.use('/Images', express_1.default.static(path_1.default.join(__dirname, 'Images')));
        app.post('/Images', upload_1.upload.single('file'), (req, res) => {
            if (!req.file) {
                res.status(400).send('No file uploaded.');
                return;
            }
            res.json({ filePath: `/Images/${req.file.filename}` });
        });
        const server = new server_1.ApolloServer({ schema: Schema_1.schema });
        yield server.start();
        app.use('/graphql', (0, cors_1.default)(), express_1.default.json(), (0, express4_1.expressMiddleware)(server, {
            context: (_a) => __awaiter(void 0, [_a], void 0, function* ({ req }) { return ({ req }); }),
        }));
        const PORT = process.env.SERVER_PORT ? parseInt(process.env.SERVER_PORT) : 3000;
        app.listen(PORT, () => {
            console.log(`🚀 Server running at http://localhost:${PORT}/graphql`);
            console.log(`📁 File uploads served at http://localhost:${PORT}/Images`);
        });
    }
    catch (error) {
        console.error('Error during Data Source initialization or server start:', error);
    }
});
main();

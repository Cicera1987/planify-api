export default () => ({
    port: parseInt(process.env.PORT ?? '3000', 10),
    mongodbURI: process.env.MONGODB_URI ?? 'mongodb://localhost:27017/planify',
});

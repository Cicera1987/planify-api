export default () => ({
  port: parseInt(process.env.PORT ?? '3001', 10),
  mongodbURI:
    process.env.MONGODB_URI ??
    'mongodb+srv://ciceraribeiro:yTwSUXAxsJqu80Qu@planify.48zvn.mongodb.net/planify?retryWrites=true&w=majority',
});

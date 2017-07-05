// users-model.js - A mongoose model
//
// See http://mongoosejs.com/docs/models.html
// for more of what you can do here.
module.exports = function (app) {
  const mongooseClient = app.get('mongooseClient');
  const { Schema } = mongooseClient;
  const users = new Schema({
    email: { type: String, required: true, unique: true },
    password: { type: String, required: true },
    name: { type: String, required: true },
    photoUrl: { type: String, default: 'caminho default' },
    createdAt: { type: Date, default: Date.now },
    updatedAt: { type: Date, default: Date.now },
    eventsCreated: [{
      event: {
        _id: { type: mongooseClient.Schema.Types.ObjectId },
        name: { type: String },
        photoUrl: { type: String },
        date: { type: Date }
      }
    }]
  });

  return mongooseClient.model('users', users);
};

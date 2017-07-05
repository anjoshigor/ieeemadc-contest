// events-model.js - A mongoose model
//
// See http://mongoosejs.com/docs/models.html
// for more of what you can do here.

module.exports = function (app) {
  const mongooseClient = app.get('mongooseClient');
  const { Schema } = mongooseClient;
  const events = new Schema({
    name: { type: String, required: true, unique: true },
    date: { type: Date },
    address: { type: String, required: true },
    description: { type: String, required: true },
    coords: { type: [Number], index: '2dsphere' },
    category: { type: String, required: true },
    createdAt: { type: Date, default: Date.now },
    updatedAt: { type: Date, default: Date.now },
    photoUrl: { type: String, default: 'caminhodefault' },
    createdBy: {
      user: {
        _id: { type: mongooseClient.Schema.Types.ObjectId },
        name: { type: String, require: true },
        email: { type: String, required: true },
        photoUrl: { type: String, require: true },
      }
    }
  });

  return mongooseClient.model('events', events);
};

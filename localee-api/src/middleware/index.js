const uploadImage = require('./upload-image');

const multer = require('multer');
const logger = require('winston');
const path = require('path');
const base = path.resolve('.');
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, base + '/public/img/')
  },
  filename: function (req, file, cb) {
    cb(null, Date.now() + '.jpg');
  }
})

const upload = multer({ storage: storage });
module.exports = function () {
  // Add your custom middleware here. Remember, that
  // in Express the order matters
  const app = this; // eslint-disable-line no-unused-vars
  logger.info("index middlewares running..");
  app.post('/users', upload.single('file'), uploadImage());
  app.post('/events', upload.single('file'), uploadImage());
};

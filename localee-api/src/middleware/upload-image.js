const logger = require('winston');


module.exports = function (options = {}) {
  return function uploadImage(req, res, next) {
    logger.info('upload-image middleware is running');
    logger.info(req.body);
    logger.info(req.file);
    req.body.photoUrl = req.protocol + '://' + req.get('host')+'/img/'+req.file.filename;
    //req.body = {photoUrl:req.file.path};
    logger.info(req.body);
    //req.params.file = req.file.filename;
    next();
  };
};

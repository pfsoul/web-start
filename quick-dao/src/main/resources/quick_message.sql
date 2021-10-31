
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for quick_message
-- ----------------------------
DROP TABLE IF EXISTS `quick_message`;
CREATE TABLE `quick_message`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `creator` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `creator_id` bigint(19) NULL DEFAULT NULL,
  `updater` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `updater_id` bigint(19) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `re_mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of quick_message
-- ----------------------------
INSERT INTO `quick_message` VALUES (1, '数据中心', '2021-10-30 18:03:37', 1, '数据中心', '2021-10-30 18:03:37', 1, 1, NULL, 'the first one');

SET FOREIGN_KEY_CHECKS = 1;

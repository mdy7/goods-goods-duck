package spharos.nu.goods.domain.goods.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageTest {

	@Test
	@DisplayName("상품 이미지 등록 여부")
	void regisImage() {

		// given
		Image image1 = Image.builder()
			.goodsCode("test")
			.url("img1")
			.index(0)
			.build();
		Image image2 = Image.builder()
			.goodsCode("test")
			.url("img2")
			.index(1)
			.build();
		Image image3 = Image.builder()
			.goodsCode("test")
			.url("img3")
			.index(2)
			.build();

		// when, then
		Assertions.assertThat(image1.getGoodsCode()).isEqualTo("test");
		Assertions.assertThat(image2.getGoodsCode()).isEqualTo("test");
		Assertions.assertThat(image3.getGoodsCode()).isEqualTo("test");

		Assertions.assertThat(image1.getIndex()).isEqualTo(0);
		Assertions.assertThat(image2.getIndex()).isEqualTo(1);
		Assertions.assertThat(image3.getIndex()).isEqualTo(2);

		Assertions.assertThat(image1.getUrl()).isEqualTo("img1");
		Assertions.assertThat(image2.getUrl()).isEqualTo("img2");
		Assertions.assertThat(image3.getUrl()).isEqualTo("img3");
	}
}
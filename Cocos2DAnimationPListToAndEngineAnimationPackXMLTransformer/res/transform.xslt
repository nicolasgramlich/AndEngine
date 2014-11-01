<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:strip-space elements="*"/>

	<xsl:template match="/plist">
		<xsl:apply-templates select="dict" mode="root"/>
	</xsl:template>

	<xsl:template match="dict" mode="root">
		<animationpack version="1">
			<xsl:apply-templates select="dict" mode="root-properties"/>
			<animations>
				<xsl:apply-templates select="dict" mode="root-animations"/>
			</animations>
		</animationpack>
	</xsl:template>

	<!--
		Finds and handles the root 'properties' dict.
	-->
	<xsl:template match="dict" mode="root-properties">
		<xsl:for-each select="preceding-sibling::key[1]">
			<xsl:if test="self::node()[. = 'properties']">
				<xsl:apply-templates select="following-sibling::dict[1]" mode="properties"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="dict" mode="properties">
		<!--
			Check if the format is equal to what we expect.
		-->
		<xsl:variable name="format" select="key[. = 'format']/following-sibling::integer[1]"/>
		<xsl:if test="$format != 2">
			<xsl:message terminate="yes">Unexpected format: '<xsl:value-of select="$format"/>' detected!</xsl:message>
		</xsl:if>

		<texturepacks>
			<xsl:for-each select="array/string">
				<!--
					Convert referenced '.plist'(cocos2d) spritesheets to '.xml' (AndEngine) texturepacks.
				-->
				<xsl:variable name="filename">
					<xsl:call-template name="replace-string">
						<xsl:with-param name="text" select="self::node()" />
						<xsl:with-param name="replace" select="'.plist'" />
						<xsl:with-param name="with" select="'.xml'" />
					</xsl:call-template>
				</xsl:variable>

				<texturepack filename="{$filename}"/>
			</xsl:for-each>
		</texturepacks>
	</xsl:template>

	<!--
		Finds and handles the root 'animations' dict.
	-->
	<xsl:template match="dict" mode="root-animations">
		<xsl:for-each select="preceding-sibling::key[1]">
			<xsl:if test="self::node()[. = 'animations']">
				<xsl:apply-templates select="following-sibling::dict[1]/key" mode="animation"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="key" mode="animation">
		<animation>
			<xsl:attribute name="name">
				<xsl:value-of select="self::node()"/>
			</xsl:attribute>

			<!--
				Handle the loops property (defaulting to 1).
			-->
			<xsl:attribute name="loopcount">
				<xsl:choose>
					<xsl:when test="count(following-sibling::dict[1]/key[. = 'loops']/following-sibling::integer[1]) > 0">
						<xsl:value-of select="following-sibling::dict[1]/key[. = 'loops']/following-sibling::integer[1]"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="1"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>

			<xsl:variable name="delayPerUnit" select="following-sibling::dict[1]/key[. = 'delayPerUnit']/following-sibling::real[1]"/>

			<!--
				Handle all frames of the current animation.
			-->
			<xsl:for-each select="following-sibling::dict[1]/key[. = 'frames']/following-sibling::array[1]/dict">
				<xsl:call-template name="animation-frame">
					<xsl:with-param name="delayPerUnit" select="$delayPerUnit"/>
				</xsl:call-template>
			</xsl:for-each>
		</animation>
	</xsl:template>

	<xsl:template name="animation-frame">
		<!--
			We require the delayPerUnit parameter,
			so that we can calculate the duration in milliseconds,
			when multiplying it with delayUnits of the current frame.
		-->
		<xsl:param name="delayPerUnit"/>

		<xsl:variable name="delayUnits" select="key[. = 'delayUnits']/following-sibling::integer[1]"/>
		<xsl:variable name="duration" select="round(1000 * number($delayPerUnit) * number($delayUnits))"/>

		<xsl:variable name="textureregion" select="key[. = 'spriteframe']/following-sibling::string[1]"/>

		<animationframe duration="{$duration}" textureregion="{$textureregion}"/>
	</xsl:template>

	<!-- String Utility -->
	<xsl:template name="replace-string">
		<xsl:param name="text" />
		<xsl:param name="replace" />
		<xsl:param name="with" />
		<xsl:choose>
			<xsl:when test="contains($text,$replace)">
				<xsl:value-of select="substring-before($text,$replace)" />
				<xsl:value-of select="$with" />
				<xsl:call-template name="replace-string">
					<xsl:with-param name="text"	select="substring-after($text,$replace)" />
					<xsl:with-param name="replace" select="$replace" />
					<xsl:with-param name="with" select="$with" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
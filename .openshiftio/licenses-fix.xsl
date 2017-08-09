<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" encoding="utf-8"/>

  <xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'"/>
  <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>

  <xsl:variable name="apache_v2_name" select="'Apache Software License, Version 2.0'"/>
  <xsl:variable name="apache_v2_url" select="'http://www.apache.org/licenses/LICENSE-2.0.txt'"/>

  <xsl:variable name="eclipse_v1_name" select="'Eclipse Public License, Version 1.0'"/>
  <xsl:variable name="eclipse_v1_url" select="'http://www.eclipse.org/legal/epl-v10.html'"/>

  <xsl:variable name="eclipse_dist_v1_name" select="'Eclipse Distribution License, Version 1.0'"/>
  <xsl:variable name="eclipse_dist_v1_url" select="'http://www.eclipse.org/org/documents/edl-v10.html'"/>

  <xsl:variable name="lgpl_v21_name" select="'GNU Lesser General Public License, Version 2.1'"/>
  <xsl:variable name="lgpl_v21_url" select="'http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html'"/>

  <xsl:variable name="bsd_2_name" select="'BSD 2-clause &quot;Simplified&quot; License'"/>
  <xsl:variable name="bsd_2_url" select="'http://www.opensource.org/licenses/BSD-2-Clause'"/>

  <xsl:variable name="bsd_3_name" select="'BSD 3-clause &quot;New&quot; or &quot;Revised&quot; License'"/>
  <xsl:variable name="bsd_3_url" select="'http://www.opensource.org/licenses/BSD-3-Clause'"/>

  <xsl:variable name="json_name" select="'JSON License'"/>
  <xsl:variable name="json_url" select="'http://www.json.org/license.html'"/>

  <xsl:variable name="mit_name" select="'The MIT License'"/>
  <xsl:variable name="mit_url" select="'https://opensource.org/licenses/mit'"/>

  <xsl:variable name="mpl_v11_name" select="'Mozilla Public License 1.1'"/>
  <xsl:variable name="mpl_v11_url" select="'http://www.mozilla.org/MPL/MPL-1.1.html'"/>

  <xsl:variable name="sax_name" select="'Sax Public Domain Notice'"/>
  <xsl:variable name="sax_url" select="'http://www.saxproject.org/copying.html'"/>

  <xsl:variable name="cddl_with_gpl_name" select="'Common Development and Distribution License (CDDL) and GNU Public License v.2 w/Classpath Exception'"/>
  <xsl:variable name="cddl_with_gpl_url" select="'https://netbeans.org/cddl-gplv2.html'"/>

  <xsl:variable name="w3c_name" select="'W3C Software Notice and License'"/>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <!-- dom4j doesn't have any license information -->
  <xsl:template match="licenses[contains(ancestor::dependency/groupId/text(), 'dom4j')]">
    <licenses>
      <xsl:call-template name="license">
        <xsl:with-param name="name" select="$bsd_3_name"/>
        <xsl:with-param name="url" select="$bsd_3_url"/>
      </xsl:call-template>
    </licenses>
  </xsl:template>

  <xsl:template match="license">
    <xsl:choose>
      <!-- Custom license modifications -->
      <xsl:when
          test="contains(ancestor::dependency/groupId/text(), 'org.javassist') and contains(translate(name/text(), $uppercase, $lowercase), 'apache')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$apache_v2_name"/>
          <xsl:with-param name="url" select="$apache_v2_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(ancestor::dependency/groupId/text(), 'org.ow2.asm')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$bsd_3_name"/>
          <xsl:with-param name="url" select="$bsd_3_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(ancestor::dependency/groupId/text(), 'org.mockito')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$mit_name"/>
          <xsl:with-param name="url" select="$mit_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(ancestor::dependency/groupId/text(), 'com.jayway.awaitility')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$apache_v2_name"/>
          <xsl:with-param name="url" select="$apache_v2_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(ancestor::dependency/groupId/text(), 'antlr')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$bsd_3_name"/>
          <xsl:with-param name="url" select="$bsd_3_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(ancestor::dependency/groupId/text(), 'com.h2database')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$eclipse_v1_name"/>
          <xsl:with-param name="url" select="$eclipse_v1_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(ancestor::dependency/groupId/text(), 'org.postgresql')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$bsd_2_name"/>
          <xsl:with-param name="url" select="$bsd_2_url"/>
        </xsl:call-template>
      </xsl:when>
      <!-- Generic license modifications -->
      <xsl:when test="contains(translate(url/text(), $uppercase, $lowercase), 'www.apache.org/licenses/license-2.0')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$apache_v2_name"/>
          <xsl:with-param name="url" select="$apache_v2_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'epl-v10')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$eclipse_v1_name"/>
          <xsl:with-param name="url" select="$eclipse_v1_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'edl-v10')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$eclipse_dist_v1_name"/>
          <xsl:with-param name="url" select="$eclipse_dist_v1_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'lgpl-2.1')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$lgpl_v21_name"/>
          <xsl:with-param name="url" select="$lgpl_v21_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'bsd-license')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$bsd_2_name"/>
          <xsl:with-param name="url" select="$bsd_2_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'json.org')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$json_name"/>
          <xsl:with-param name="url" select="$json_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'mit-license')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$mit_name"/>
          <xsl:with-param name="url" select="$mit_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'mpl-1.1')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$mpl_v11_name"/>
          <xsl:with-param name="url" select="$mpl_v11_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'saxproject')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$sax_name"/>
          <xsl:with-param name="url" select="$sax_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'cddl + gplv2 with classpath exception')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$cddl_with_gpl_name"/>
          <xsl:with-param name="url" select="$cddl_with_gpl_url"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains(translate(., $uppercase, $lowercase), 'w3c')">
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="$w3c_name"/>
          <xsl:with-param name="url" select="url/text()"/>
        </xsl:call-template>
      </xsl:when>
      <!-- If nothing matches, leave original values -->
      <xsl:otherwise>
        <xsl:call-template name="license">
          <xsl:with-param name="name" select="name/text()"/>
          <xsl:with-param name="url" select="url/text()"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="license">
    <xsl:param name="name"/>
    <xsl:param name="url"/>
    <license>
      <name><xsl:value-of select="$name"/></name>
      <url><xsl:value-of select="$url"/></url>
    </license>
  </xsl:template>


</xsl:stylesheet>

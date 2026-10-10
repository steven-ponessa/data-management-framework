---
title: New CIO CI/CD Pipeline
permalink: /en/quickstart/cio-ci-cd-pipeline-new
features:
  - mermaid
  - d3
  - select2
  - datatables
abstract: >- # this means to ignore newlines until "baseurl:"
    The CIO CI/CD pipeline is a custom-built DevSecOps solution, designed to enable CIO engineers to deploy software into the Cirrus platform as easily as possible, while still conforming to all the CIO specific requirements and regulations. The goal of the pipeline is to allow engineers and managers to concentrate on development while allowing automation to perform the toil of deployment.
---

# Overview

_put content here_

# Deploying the WFM-DMF to production

1. Go to CIO Cirrus -> My Developer Portal (Applications: Professional Market Place -> wfm-dmf-ga)
[https://cirrus.ibm.com/developer/application/SERVICENOW_ZIM-000001/GHE_1711065?repoTab=deployments](https://cirrus.ibm.com/developer/application/SERVICENOW_ZIM-000001/GHE_1711065?repoTab=deployments)

2. From "**Actions**" button (upper right hand corner), select **Release code**
3. In **Environment to promote**, select **pre-production (0.0.##) - dd Mmm yyyy**
4. In **Release location**, select **production (0.0.##) - dd Mmm yyyy***
5. Click **Deploy**
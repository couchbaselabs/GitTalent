export interface IOrganization {
    id? : string,
    name : string,
    address : IAddress,
    website : string
}

export interface IUser {
    id?: string,
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    phone: string
}

export interface IAddress {
    gis : ILatLon,
    street : string,
    city : string,
    country : string
}

export interface ILatLon {
    latitude : number,
    longitude: number
}

export interface ITicket {
    id? : string,
    status: string,
    title: string,
    createdAt: number
}

export interface IDeveloperInfo {
    firstName: string,
    lastName: string,
    company: string,
    email: string,
    phone: string,
    avatarURL: string,
    repositoryCount: number,
    followers: number,
    follows: number,
    socialMedia?: any
}

export interface IDeveloper {
    id?: string,
    developerInfo: IDeveloperInfo,
    address: IAddress,
    contacts: Array<IDeveloper>,
    history: Array<ITicket>,
    createdAt: string
}

export interface IHit {
    fragments: Array<string>,
    id: string,
    score: number,
    type: string,
    assignedId?: string,
    email?: string;
    firstName?: string;
}

export interface ITerm {
  term: string,
  count: number
}

export interface IDDFacet {
  name: string,
  field: string,
  missing: number,
  other: number,
  total: number,
  type: string
}
export interface IDDNumericFacet extends IDDFacet {
  facets: Array<INumericFacet>
}

export interface IDDTermFacet extends IDDFacet {
  facets: Array<ITermFacet>
}


export interface INumericFacet extends ITermFacet{
  min: number,
  max: number,
  name: string
}

export interface ITermFacet {
  term: string,
  count: number
}

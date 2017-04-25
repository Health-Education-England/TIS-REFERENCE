import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {SiteDetailComponent} from "../../../../../../main/webapp/app/entities/site/site-detail.component";
import {SiteService} from "../../../../../../main/webapp/app/entities/site/site.service";
import {Site} from "../../../../../../main/webapp/app/entities/site/site.model";

describe('Component Tests', () => {

	describe('Site Management Detail Component', () => {
		let comp: SiteDetailComponent;
		let fixture: ComponentFixture<SiteDetailComponent>;
		let service: SiteService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [SiteDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					SiteService
				]
			}).overrideComponent(SiteDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(SiteDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(SiteService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Site(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.site).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});

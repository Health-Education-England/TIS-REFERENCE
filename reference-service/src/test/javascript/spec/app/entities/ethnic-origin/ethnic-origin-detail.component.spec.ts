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
import {EthnicOriginDetailComponent} from "../../../../../../main/webapp/app/entities/ethnic-origin/ethnic-origin-detail.component";
import {EthnicOriginService} from "../../../../../../main/webapp/app/entities/ethnic-origin/ethnic-origin.service";
import {EthnicOrigin} from "../../../../../../main/webapp/app/entities/ethnic-origin/ethnic-origin.model";

describe('Component Tests', () => {

	describe('EthnicOrigin Management Detail Component', () => {
		let comp: EthnicOriginDetailComponent;
		let fixture: ComponentFixture<EthnicOriginDetailComponent>;
		let service: EthnicOriginService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [EthnicOriginDetailComponent],
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
					EthnicOriginService
				]
			}).overrideComponent(EthnicOriginDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(EthnicOriginDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(EthnicOriginService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new EthnicOrigin(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.ethnicOrigin).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
